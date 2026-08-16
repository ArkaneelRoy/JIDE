#!/usr/bin/env bash
#
# Prepare the APKs produced by :core:app:assembleRelease for publication.
#
# This script intentionally fails closed: a release must contain one APK for
# every supported ABI and checksums for every published APK.
set -Eeuo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
OUTPUT_DIR="${ROOT_DIR}/core/app/build/outputs/apk/release"
DIST_DIR="${1:-${ROOT_DIR}/dist}"
RELEASE_VERSION="${RELEASE_VERSION:-${GITHUB_REF_NAME:-}}"

if [[ -z "${RELEASE_VERSION}" ]]; then
  echo "RELEASE_VERSION or GITHUB_REF_NAME must be set" >&2
  exit 2
fi

RELEASE_VERSION="${RELEASE_VERSION#v}"
if [[ ! "${RELEASE_VERSION}" =~ ^[0-9]+\.[0-9]+\.[0-9]+([-.][0-9A-Za-z.-]+)?$ ]]; then
  echo "Invalid release version: ${RELEASE_VERSION}" >&2
  exit 2
fi

mkdir -p "${DIST_DIR}"
rm -f "${DIST_DIR}"/*.apk "${DIST_DIR}"/*CHECKSUMS.txt

abis=("arm64-v8a" "armeabi-v7a" "x86_64")
for abi in "${abis[@]}"; do
  expected="${OUTPUT_DIR}/app-${abi}-release.apk"
  apk=""

  if [[ -f "${expected}" ]]; then
    apk="${expected}"
  else
    mapfile -t matches < <(find "${OUTPUT_DIR}" -maxdepth 1 -type f -name "*${abi}*release*.apk" -print | sort)
    if [[ "${#matches[@]}" -eq 1 ]]; then
      apk="${matches[0]}"
    fi
  fi

  if [[ -z "${apk}" || ! -f "${apk}" ]]; then
    echo "Missing or ambiguous ${abi} release APK in ${OUTPUT_DIR}" >&2
    find "${OUTPUT_DIR}" -maxdepth 1 -type f -name '*.apk' -print 2>/dev/null | sort >&2 || true
    exit 1
  fi

  cp -- "${apk}" "${DIST_DIR}/androidide-${RELEASE_VERSION}-${abi}.apk"
done

( cd "${DIST_DIR}" && md5sum ./*.apk > "androidide-ultra-${RELEASE_VERSION}-md5-CHECKSUMS.txt" )
( cd "${DIST_DIR}" && sha256sum ./*.apk > "androidide-ultra-${RELEASE_VERSION}-sha256-CHECKSUMS.txt" )
( cd "${DIST_DIR}" && sha512sum ./*.apk > "androidide-ultra-${RELEASE_VERSION}-sha512-CHECKSUMS.txt" )

printf 'Prepared release assets in %s:\n' "${DIST_DIR}"
find "${DIST_DIR}" -maxdepth 1 -type f -printf '  %f\n' | sort
