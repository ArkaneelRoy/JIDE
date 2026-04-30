# 🏗️ AndroidIDE Ultra: Compose Preview Engine Architecture

To achieve parity with Android Studio's Preview system, **AndroidIDE Ultra (AIDEU)** requires a sophisticated rendering engine that can execute and display Jetpack Compose code directly on the device.

---

## 1. The Challenge
Android Studio renders Compose Previews on the desktop JVM using **Layoutlib** (a specialized version of the Android framework). On a mobile device, we have the advantage of running on the actual Android OS, but we face challenges with **dynamic class loading** and **rendering isolated components** without a full app launch.

## 2. Proposed Architecture: The "Hot-Swap Renderer"

We will implement a three-tier architecture to handle Compose rendering:

### A. The Tooling Artifact (`aideu-compose-tooling`)
A small library injected into the user's project during the build process.
- **Preview Discovery**: Scans for functions annotated with `@Preview`.
- **Reflection Bridge**: Provides a stable API for the IDE to invoke private/internal Compose functions.
- **Isolated Host**: A specialized `Activity` or `Fragment` that can host a single Composable in a clean environment.

### B. The Dynamic Class Loader
A custom `ClassLoader` within AIDEU that:
- Loads the freshly compiled `.dex` files from the user's build directory.
- Resolves dependencies from the local Maven cache and SDK.
- Handles "Hot-Swapping" by clearing the cache and reloading classes when code changes are detected.

### C. The Rendering Pipeline
1. **Trigger**: User edits a Composable or manually refreshes the preview.
2. **Incremental Compile**: Gradle (or a custom fast-compiler) generates a DEX file for the modified module.
3. **Injection**: AIDEU's Preview Service loads the DEX and identifies the target `@Preview` function.
4. **Composition**: The function is executed within a `ComposeView` attached to an off-screen `Surface`.
5. **Display**: The resulting UI is rendered into a `TextureView` or `SurfaceView` within the IDE's preview pane.

---

## 3. Implementation Milestones

### Milestone 1: Static DEX Execution
- Successfully load a pre-compiled DEX file containing a simple Composable.
- Execute it within the IDE's process and display it in a basic view.

### Milestone 2: Preview Parameter Support
- Support `@PreviewParameter` and multiple `@Preview` annotations.
- Implement configuration switching (Dark Mode, RTL, Font Scale).

### Milestone 3: Live Edit (The "Ultra" Experience)
- Integrate with the file watcher to trigger background incremental builds.
- Implement "Dirty State" detection to only reload what's necessary.

---

## 4. Technical Comparison

| Feature | Android Studio (Desktop) | AIDEU (Mobile) |
| :--- | :--- | :--- |
| **Rendering Engine** | Layoutlib (JVM Simulation) | Native Android Framework |
| **Class Loading** | Desktop ClassLoader | Custom DexClassLoader |
| **Performance** | High (Powerful CPUs) | Extreme (Native execution, no simulation) |
| **Accuracy** | Good (Simulated) | Perfect (Actual OS & Hardware) |

---
*This document serves as the technical blueprint for the AIDEU Compose Preview Engine.*
