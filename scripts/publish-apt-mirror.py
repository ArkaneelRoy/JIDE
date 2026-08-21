#!/usr/bin/env python3
"""Build a minimal, signed AndroidIDE Ultra APT mirror from a pinned upstream snapshot.

The upstream AndroidIDE repository is archived. This script mirrors only the package
closure needed by idesetup.sh, verifies every downloaded .deb against the upstream
Packages SHA-256, and leaves signing of Release/InRelease to the CI workflow.
"""
from __future__ import annotations

import argparse
import hashlib
import re
import shutil
import subprocess
import time
import sys
import urllib.request
from pathlib import Path

UPSTREAM = "https://packages.androidide.com/apt/termux-main"
BASE_PACKAGES = {"apt", "ca-certificates", "jq", "tar", "git", "openssh", "libcurl"}
ARCHES = {"aarch64": "aarch64", "arm": "arm", "x86_64": "x86_64"}
BOOTSTRAP_PROVIDED = {"termux-keyring", "termux-licenses", "termux-tools"}


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
    digest = hashlib.sha256(deb.read_bytes()).hexdigest()
    return {
        "Package": "ca-certificates",
        "Architecture": "all",
        "Version": "2026.1",
        "Section": "misc",
        "Priority": "required",
        "Maintainer": "AndroidIDE Ultra",
        "Description": "AndroidIDE Ultra bootstrap certificate compatibility package\n The Termux bootstrap already supplies the certificate bundle at /etc/tls/cert.pem.",
        "Filename": "dists/stable/main/binary-all/ca-certificates_2026.1_all.deb",
        "Size": str(deb.stat().st_size),
        "SHA256": digest,
    }


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
                actual = hashlib.sha256(destination.read_bytes()).hexdigest()
                if actual != sha256:
                    raise SystemExit(f"{arch}: SHA-256 mismatch for {name}: {actual} != {sha256}")
                data["Filename"] = f"dists/stable/main/binary-{arch}/{destination.name}"
            selected.append(data)
        packages = []
        for data in selected:
            packages.append("\n".join(f"{k}: {v}" for k, v in data.items()))
        (output_dir / "Packages").write_text("\n\n".join(packages) + "\n")
        subprocess.run(["gzip", "-9", "-c", str(output_dir / "Packages")], check=True, stdout=(output_dir / "Packages.gz").open("wb"))
        print(f"{arch}: mirrored {len(selected)} packages")

    for path in root.rglob("upstream-*.Packages"):
        path.unlink()
    (root / "apt" / "termux-main" / "dists" / "stable" / "Release.template").write_text(
        "Origin: AndroidIDE Ultra\nLabel: AndroidIDE Ultra\nSuite: stable\nCodename: termux\nArchitectures: aarch64 arm x86_64\nComponents: main\nDescription: AndroidIDE Ultra Termux packages\n"
    )


if __name__ == "__main__":
    main()
