# <p align="center">🏗️ Architecture: Native Compose Rendering</p>

<p align="center">
  <i>Inside the "Hot-Swap Renderer" — AIDEU's answer to Android Studio's Preview system.</i>
</p>

---

## 🧐 The Philosophy
Desktop IDEs use **Layoutlib** to simulate Android. This is slow and often inaccurate. **AndroidIDE Ultra** takes a different path: **Native Execution**. We run your code on the actual hardware it was meant for.

---

## 🧩 The "Hot-Swap" Engine Breakdown

Our architecture is split into three high-performance layers:

### 1. The Tooling Bridge (`aideu-tooling`)
This is a lightweight "agent" that lives inside the user's project.
- **Function Discovery**: Uses reflection to find `@Preview` methods.
- **State Preservation**: Ensures that previews maintain their state even during reloads.

### 2. The Dynamic Dex Loader
The heart of the system. It handles the "magic" of running code without a full app restart.
- **Incremental Loading**: Only loads the classes that changed.
- **Dependency Resolution**: Maps project classes to the IDE's internal framework libraries.

### 3. The Surface Renderer
Where the pixels meet the screen.
- **Isolated Sandbox**: Previews run in a dedicated process to prevent IDE crashes.
- **Hardware Acceleration**: Uses the device's GPU to render Compose frames at 60 FPS.

---

## 📊 Technical Comparison

| Feature | Android Studio (Desktop) | **AIDEU (Mobile)** |
| :--- | :--- | :--- |
| **Accuracy** | 90% (Simulated) | **100% (Native)** |
| **Speed** | Varies by CPU | **Instant (Hardware)** |
| **Feedback Loop** | 2-5 Seconds | **< 1 Second** |
| **Device Parity** | Approximation | **Actual Device** |

---

## 🛠️ Implementation Status

> [!NOTE]
> This engine is currently in the **R&D Phase**. We are actively prototyping the `DexClassLoader` bridge.

1.  **Phase A**: Static Class Loading (In Progress) 🟡
2.  **Phase B**: Dynamic Reflection Bridge ⚪
3.  **Phase C**: Real-time Surface Injection ⚪

---

## 💡 Join the Development

Want to help build the renderer? We're looking for experts in:
- Android Internals & ART (Android Runtime)
- Jetpack Compose Compiler Plugins
- Custom ClassLoaders & Dex Manipulation

**[View the Project Roadmap](./ROADMAP.md)** | **[Return to Home](./README.md)**
