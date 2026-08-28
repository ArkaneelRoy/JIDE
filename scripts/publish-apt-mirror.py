#!/usr/bin/env python3
"""Build a minimal, signed AndroidIDE Ultra APT mirror from a pinned upstream snapshot.

The upstream AndroidIDE repository is archived. This script mirrors only the package
closure needed by idesetup.sh, verifies every downloaded .deb against the upstream
Packages SHA-256, relocates legacy absolute package roots to the Termux prefix root,
and leaves signing of Release/InRelease to the CI workflow.
"""
from __future__ import annotations

import argparse
import hashlib
import os
import re
import shutil
import subprocess
import tempfile
from pathlib import Path

UPSTREAM = "https://packages.androidide.com/apt/termux-main"
BASE_PACKAGES = {"apt", "ca-certificates", "jq", "tar", "git", "openssh", "libcurl", "openjdk-17", "openjdk-21"}
ARCHES = {"aarch64": "aarch64", "arm": "arm", "x86_64": "x86_64"}
BOOTSTRAP_PROVIDED = {"termux-keyring", "termux-licenses", "termux-tools"}
LEGACY_PACKAGE = b"com.itsaky.androidide"
CURRENT_PACKAGE = b"com.willow.androidide"
# The prebuilt binaries have the upstream paths compiled into them. Substituting inside an
# ELF image only works while the replacement is exactly as long as what it replaces, which
# is why the application ID is pinned to 21 characters. Keep this in sync with
# BuildConfig.applicationId; lengthening it breaks every prebuilt binary.
assert len(CURRENT_PACKAGE) == len(LEGACY_PACKAGE), (
    f"package name length changed ({len(LEGACY_PACKAGE)} -> {len(CURRENT_PACKAGE)}): "
    "prebuilt binaries can no longer be patched in place"
)
# Substituting the package name rather than a single prefix covers every path built from it
# -- files/usr, files/home, cache -- under both the /data/data and /data/user/0 spellings.
LEGACY_PREFIX = b"/data/data/" + LEGACY_PACKAGE + b"/files/usr"
CURRENT_PREFIX = b"/data/data/" + CURRENT_PACKAGE + b"/files/usr"
LEGACY_MEMBER_ROOT = Path(LEGACY_PREFIX.decode().lstrip("/"))
CURRENT_MEMBER_ROOT = Path(CURRENT_PREFIX.decode().lstrip("/"))


def rewrite_payload_file(path: Path):
    if path.is_symlink():
        target = os.readlink(path)
        rewritten = target.replace(LEGACY_PACKAGE.decode(), CURRENT_PACKAGE.decode())
        if rewritten != target:
            path.unlink()
            path.symlink_to(rewritten)
        return
    if not path.is_file():
        return
    raw = path.read_bytes()
    rewritten = raw.replace(LEGACY_PACKAGE, CURRENT_PACKAGE)
    if rewritten != raw:
        path.write_bytes(rewritten)


def relocate_deb_prefix(deb: Path):
    """Rewrite archive member paths and text/symlink targets to this fork's prefix."""
    with tempfile.TemporaryDirectory(prefix="androidide-deb-") as temp_name:
        extracted = Path(temp_name) / "root"
        rebuilt = Path(temp_name) / deb.name
        subprocess.run(["dpkg-deb", "--raw-extract", str(deb), str(extracted)], check=True, stdout=subprocess.DEVNULL)
        old_root = extracted / LEGACY_MEMBER_ROOT
        new_root = extracted / CURRENT_MEMBER_ROOT
        if old_root.exists():
            if new_root.exists() or new_root.is_symlink():
                raise SystemExit(f"cannot relocate {deb}: payload collision at {new_root}")
            new_root.parent.mkdir(parents=True, exist_ok=True)
            old_root.rename(new_root)
            # Drop the legacy ancestors, stopping at one shared with the new root.
            directory = old_root.parent
            while directory != extracted and not any(directory.iterdir()):
                directory.rmdir()
                directory = directory.parent
        for path in extracted.rglob("*"):
            rewrite_payload_file(path)
        conffiles = extracted / "DEBIAN/conffiles"
        if conffiles.exists():
            kept = []
            for line in conffiles.read_bytes().splitlines():
                value = line.strip()
                if not value:
                    continue
                target = extracted / value.lstrip(b"/").decode("utf-8", errors="strict")
                if target.exists() or target.is_symlink():
                    kept.append(value + b"\n")
            conffiles.write_bytes(b"".join(kept))
        for path in extracted.rglob("*"):
            if path.is_symlink():
                if LEGACY_PACKAGE.decode() in os.readlink(path):
                    raise SystemExit(f"{deb}: legacy package name survives in symlink {path}")
            elif path.is_file() and LEGACY_PACKAGE in path.read_bytes():
                raise SystemExit(f"{deb}: legacy package name survives in {path}")
        subprocess.run(["dpkg-deb", "--build", "--root-owner-group", str(extracted), str(rebuilt)], check=True, stdout=subprocess.DEVNULL)
        shutil.copy2(rebuilt, deb)



HASH_FIELDS = {
    "MD5Sum": "md5",
    "SHA1": "sha1",
    "SHA256": "sha256",
    "SHA512": "sha512",
}


def file_hashes(path: Path):
    """Return the APT digest fields for ``path``, computed from its current bytes."""
    digests = {field: hashlib.new(algorithm) for field, algorithm in HASH_FIELDS.items()}
    with path.open("rb") as handle:
        for chunk in iter(lambda: handle.read(1024 * 1024), b""):
            for digest in digests.values():
                digest.update(chunk)
    return {field: digest.hexdigest() for field, digest in digests.items()}


def set_hash_fields(data: dict, path: Path):
    """Replace every digest field in ``data`` with one computed from ``path``."""
    for key in [k for k in data if k.lower() in {f.lower() for f in HASH_FIELDS}]:
        del data[key]
    data.update(file_hashes(path))


def verify_index(index: Path, repository: Path):
    """Fail the build if any published digest disagrees with the file it describes."""
    for paragraph in paragraphs(index.read_text()):
        data = fields(paragraph)
        deb = repository / data["Filename"]
        if not deb.is_file():
            raise SystemExit(f"{index}: {data['Package']} references missing file {deb}")
        if data["Size"] != str(deb.stat().st_size):
            raise SystemExit(f"{index}: {data['Package']} has a stale Size field")
        actual = file_hashes(deb)
        for field, value in actual.items():
            if data.get(field, value) != value:
                raise SystemExit(f"{index}: {data['Package']} has a stale {field} field")


def paragraphs(text: str):
    return [p for p in re.split(r"\n\s*\n", text.strip()) if p.strip()]


def fields(paragraph: str):
    result = {}
    for line in paragraph.splitlines():
        if ": " in line:
            key, value = line.split(": ", 1)
            result[key] = value
    return result


def dependency_names(value: str):
    names = []
    for item in value.split(","):
        alternative = item.split("|")[0].strip()
        alternative = re.sub(r"\s*\([^)]*\)", "", alternative)
        alternative = alternative.split(":", 1)[0].strip()
        if alternative and re.fullmatch(r"[A-Za-z0-9+_.-]+", alternative):
            names.append(alternative)
    return names


def download(url: str, destination: Path):
    destination.parent.mkdir(parents=True, exist_ok=True)
    destination.unlink(missing_ok=True)
    subprocess.run(
        [
            "curl", "--fail", "--location", "--retry", "3", "--retry-all-errors",
            "--connect-timeout", "30", "--max-time", "600",
            "-A", "AndroidIDE-Ultra-APT-Mirror/1.0 (+https://github.com/Willow7737/AndroidIDE-Ultra)",
            "--output", str(destination), url,
        ],
        check=True,
    )


def make_bootstrap_certificate_package(root: Path):
    package_dir = root / "apt" / "termux-main" / "dists" / "stable" / "main" / "binary-all"
    package_dir.mkdir(parents=True, exist_ok=True)
    build_dir = root / "bootstrap-ca-certificates"
    control_dir = build_dir / "DEBIAN"
    control_dir.mkdir(parents=True, exist_ok=True)
    (control_dir / "control").write_text(
        "Package: ca-certificates\n"
        "Version: 2026.1\n"
        "Section: misc\n"
        "Priority: required\n"
        "Architecture: all\n"
        "Maintainer: AndroidIDE Ultra\n"
        "Description: AndroidIDE Ultra bootstrap certificate compatibility package\n"
        " The Termux bootstrap already supplies the certificate bundle at /etc/tls/cert.pem.\n"
    )
    deb = package_dir / "ca-certificates_2026.1_all.deb"
    subprocess.run(["dpkg-deb", "--build", "--root-owner-group", str(build_dir), str(deb)], check=True, stdout=subprocess.DEVNULL)
    entry = {
        "Package": "ca-certificates",
        "Architecture": "all",
        "Version": "2026.1",
        "Section": "misc",
        "Priority": "required",
        "Maintainer": "AndroidIDE Ultra",
        "Description": "AndroidIDE Ultra bootstrap certificate compatibility package\n The Termux bootstrap already supplies the certificate bundle at /etc/tls/cert.pem.",
        "Filename": "dists/stable/main/binary-all/ca-certificates_2026.1_all.deb",
        "Size": str(deb.stat().st_size),
    }
    set_hash_fields(entry, deb)
    return entry


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--output", type=Path, required=True)
    args = parser.parse_args()
    root = args.output.resolve()
    if root.exists():
        shutil.rmtree(root)
    root.mkdir(parents=True)
    bootstrap_certificate = make_bootstrap_certificate_package(root)

    for arch in ARCHES:
        index_url = f"{UPSTREAM}/dists/stable/main/binary-{arch}/Packages"
        index_path = root / f"upstream-{arch}.Packages"
        download(index_url, index_path)
        entries = {}
        for paragraph in paragraphs(index_path.read_text(errors="replace")):
            data = fields(paragraph)
            if data.get("Package"):
                entries[data["Package"]] = data
        entries["ca-certificates"] = bootstrap_certificate
        required = set(BASE_PACKAGES) - BOOTSTRAP_PROVIDED
        queue = list(required)
        while queue:
            name = queue.pop()
            if name not in entries:
                if name in BOOTSTRAP_PROVIDED:
                    continue
                raise SystemExit(f"{arch}: required dependency {name!r} is absent from upstream index")
            for dep in dependency_names(entries[name].get("Depends", "")):
                if dep not in required:
                    required.add(dep)
                    queue.append(dep)
        output_dir = root / "apt" / "termux-main" / "dists" / "stable" / "main" / f"binary-{arch}"
        output_dir.mkdir(parents=True)
        selected = []
        for name in sorted(required):
            if name in BOOTSTRAP_PROVIDED:
                continue
            data = dict(entries[name])
            filename = data.get("Filename")
            sha256 = data.get("SHA256")
            if not filename or not sha256:
                raise SystemExit(f"{arch}: incomplete metadata for {name}")
            if name != "ca-certificates":
                source_url = f"{UPSTREAM}/{filename}"
                destination = output_dir / Path(filename).name
                download(source_url, destination)
                downloaded = hashlib.sha256(destination.read_bytes()).hexdigest()
                if downloaded != sha256:
                    raise SystemExit(f"{arch}: {name} download digest {downloaded} does not match upstream {sha256}")
                relocate_deb_prefix(destination)
                data["Filename"] = f"dists/stable/main/binary-{arch}/{destination.name}"
                data["Size"] = str(destination.stat().st_size)
                # Relocating rewrote the archive, so every upstream digest is now stale.
                set_hash_fields(data, destination)
            selected.append(data)
        packages = []
        for data in selected:
            packages.append("\n".join(f"{k}: {v}" for k, v in data.items()))
        (output_dir / "Packages").write_text("\n\n".join(packages) + "\n")
        verify_index(output_dir / "Packages", root / "apt" / "termux-main")
        subprocess.run(["gzip", "-9", "-c", str(output_dir / "Packages")], check=True, stdout=(output_dir / "Packages.gz").open("wb"))
        print(f"{arch}: mirrored {len(selected)} packages")

    for path in root.rglob("upstream-*.Packages"):
        path.unlink()
    (root / "apt" / "termux-main" / "dists" / "stable" / "Release.template").write_text(
        "Origin: AndroidIDE Ultra\nLabel: AndroidIDE Ultra\nSuite: stable\nCodename: termux\nArchitectures: aarch64 arm x86_64\nComponents: main\nDescription: AndroidIDE Ultra Termux packages\n"
    )


if __name__ == "__main__":
    main()
