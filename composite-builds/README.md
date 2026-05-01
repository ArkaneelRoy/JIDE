# 🧩 Composite Builds in AndroidIDE Ultra

The AndroidIDE Ultra project utilizes **Composite Builds** to integrate multiple independent Gradle builds into a single, cohesive build process. This architectural choice is fundamental to managing the complexity of a mobile-first IDE, as it allows for the isolation of build logic, shared dependencies, and custom plugins from the core application source code. By leveraging this feature, the project maintains a clean separation of concerns and improves the maintainability of the entire build ecosystem.

## 📂 Organizational Structure

The `composite-builds` directory is organized into three primary functional areas, each serving a distinct role in the development lifecycle.

| Component | Primary Function | Description |
| :--- | :--- | :--- |
| **build-deps** | Dependency Management | Houses external dependencies that are compiled from source. This ensures that AndroidIDE Ultra can utilize specific or patched versions of libraries that are critical for its operation. |
| **build-deps-common** | Shared Resources | Contains utility modules and common code shared between the main application build and other composite builds, preventing logic duplication. |
| **build-logic** | Build Orchestration | Serves as the central repository for custom Gradle plugins and build-time configurations. It manages complex tasks such as bytecode desugaring and automated asset processing. |

## 🚀 Strategic Advantages

Implementing composite builds provides several strategic advantages for the project. **Modularization** is the most significant benefit, as it keeps the "how" of the build separate from the "what" of the application. This separation allows developers to focus on feature development without being overwhelmed by the underlying build infrastructure.

Furthermore, this approach enhances **build performance** and **reliability**. Gradle can efficiently manage and cache the outputs of included builds, leading to faster incremental builds. It also simplifies the process of testing build-logic changes, as plugins can be developed and verified in isolation before being applied to the entire project. This robust structure ensures that AndroidIDE Ultra remains scalable as more features and platforms are added to the roadmap.

---
*For more information on how to contribute to the build system, please refer to the [CONTRIBUTING.md](../CONTRIBUTING.md) guide.*
