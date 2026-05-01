# 📦 AAPT2 Protobuf Definitions

This directory contains the Protocol Buffer (Protobuf) definition files for **AAPT2** (Android Asset Packaging Tool 2). These definitions are essential for AndroidIDE Ultra to interact with the Android resource system and perform binary resource compilation and linking natively on the device.

## 📄 Source Information

The files in this directory are sourced directly from the official [Android Open Source Project (AOSP)](https://cs.android.com/android/platform/superproject/main/+/main:frameworks/base/tools/aapt2/).

- **Upstream Path**: `frameworks/base/tools/aapt2/`
- **License**: [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)

## 🛠️ Usage in AIDEU

These proto files are used to generate Java/Kotlin classes that allow the IDE to:
1.  **Parse** compiled resource files.
2.  **Communicate** with the AAPT2 daemon.
3.  **Inspect** and manipulate the resource table during the build process.

By maintaining these definitions, AndroidIDE Ultra ensures compatibility with the standard Android build pipeline and enables advanced features like the visual layout inspector and resource autocomplete.

---
*Note: These files should be kept in sync with the AOSP upstream to maintain compatibility with newer Android SDK versions.*
