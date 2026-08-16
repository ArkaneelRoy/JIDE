# <p align="center">🚀 AndroidIDE Ultra (AIDEU)</p>
<p align="center">
  <i>The mobile-first IDE that rivals the desktop. Built for the community, by the community.</i>
</p>

<p align="center">
  <img src="https://img.shields.io/github/v/release/Willow7737/AndroidIDE-Ultra?include_prereleases&style=for-the-badge&color=blueviolet" alt="Latest Release">
  <img src="https://img.shields.io/github/license/Willow7737/AndroidIDE-Ultra?style=for-the-badge" alt="License">
  <img src="https://img.shields.io/github/stars/Willow7737/AndroidIDE-Ultra?style=for-the-badge&color=gold" alt="Stars">
</p>

---

## 🌟 What is AndroidIDE Ultra?

**AndroidIDE Ultra** is a high-performance, community-driven fork of the original AndroidIDE project. While the original project laid the foundation, **Ultra** aims to push the boundaries of what's possible on a mobile device, bringing **Android Studio-grade features** to your pocket.

> [!IMPORTANT]
> **AndroidIDE Ultra** is an independent community effort. We are not affiliated with the original authors, but we carry their legacy forward with modern updates and aggressive feature development.

---

## 💎 The "Ultra" Difference

Why choose Ultra? We aren't just maintaining; we are **innovating**.

| Feature | AndroidIDE (Legacy) | **AndroidIDE Ultra** |
| :--- | :---: | :---: |
| **Compose Preview** | ❌ | ✅ **Native Engine** |
| **AI Integration** | ❌ | ✅ **Claude Code** |
| **Cross-Platform** | ❌ | ✅ **Flutter Support** |
| **Modern AGP** | Limited | ✅ **Full Support (8.5+)** |
| **Performance** | Standard | ✅ **Optimized Dexing** |

---

## 🛠️ Core Capabilities

*   **Native Gradle Engine**: No cloud, no compromises. Build real APKs/AABs directly on your device.
*   **Intelligent Editor**: Powered by Language Servers for Java, XML, and (soon) Kotlin.
*   **Terminal Power**: A full-featured terminal with `git`, `ssh`, and `sdkmanager`.
*   **Asset Studio**: Generate icons and drawables on the fly.

---

## 🗺️ The Vision (Roadmap)

We have big plans. From **Jetpack Compose Previews** that run natively to **Claude Code** integration for autonomous programming.

👉 **[Explore the Full Roadmap 1.0](./ROADMAP.md)**
👉 **[View Recent Contributions](./CONTRIBUTIONS.md)**

---

## 🏗️ Technical Architecture

Curious about how we're making Compose Previews work natively on Android? We've documented our unique "Hot-Swap Renderer" approach.

👉 **[Deep Dive into the Architecture](./COMPOSE_PREVIEW_ARCH.md)**

---

## 📥 Getting Started

1.  **Download**: Grab the latest APK from [Releases](https://github.com/Willow7737/AndroidIDE-Ultra/releases).
2.  **Install SDK**: Use the built-in terminal to download your target Android SDKs.
3.  **Build**: Open your first project and experience the power of desktop-class dev on mobile.

---

## 🚀 Build and Release

AndroidIDE Ultra is built with the Gradle wrapper and requires **JDK 17** plus an Android SDK. Run `scripts/setup_dev_env.sh` to check the local environment, then use `./gradlew :core:app:assembleDebug` for a development APK or `./gradlew :core:app:assembleRelease` for release APKs. Release builds are split into the supported `arm64-v8a`, `armeabi-v7a`, and `x86_64` ABIs.

The repository has two release paths. The existing `Build and test` workflow validates changes and retains its Nyx-based main-branch publication flow. The dedicated `Release AndroidIDE Ultra` workflow is the deterministic path for publishing a GitHub release: push a `v`-prefixed semantic-version tag such as `v2.7.1`, or manually dispatch the workflow with an existing tag. It builds and verifies all three ABI APKs, generates MD5, SHA-256, and SHA-512 checksums, and uploads the assets to the matching GitHub release. If signing secrets are configured, the release uses the project signing key; otherwise the build system’s documented fallback behavior applies.

For local release-asset preparation after a successful Gradle build, run `RELEASE_VERSION=v2.7.1 scripts/prepare_release_assets.sh dist`. The script fails if an ABI APK is missing or ambiguous and verifies the published checksum set before the workflow creates a release.

## ⚖️ License & Attribution

AndroidIDE Ultra is licensed under the **GNU General Public License v3.0**. 
This project is a derivative work of the original [AndroidIDE](https://github.com/AndroidIDEOfficial/AndroidIDE). We honor and respect the work of the original contributors who made this journey possible.