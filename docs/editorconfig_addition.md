# Added .editorconfig for Consistent Code Style

To ensure consistent code formatting across the AndroidIDE Ultra project, an `.editorconfig` file has been added to the repository. This file helps maintain a unified coding style, which is essential for collaborative development and code readability.

## Benefits of .editorconfig

*   **Consistent Formatting**: Automatically enforces predefined coding styles (indentation, line endings, etc.) regardless of the editor or IDE used by individual contributors.
*   **Improved Code Readability**: A uniform code style makes the codebase easier to read and understand for all developers.
*   **Reduced Merge Conflicts**: Minimizes conflicts arising from differing formatting preferences during pull requests.
*   **Easier Onboarding**: New contributors can quickly adapt to the project's coding standards without manual configuration.

## Configuration Details

The `.editorconfig` file is configured to enforce the following rules, aligning with the guidelines specified in `CONTRIBUTING.md`:

| Setting | Value |
| :------ | :---- |
| `indent_style` | `space` |
| `indent_size` | `2` |
| `charset` | `utf-8` |
| `trim_trailing_whitespace` | `true` |
| `insert_final_newline` | `true` |

Specific overrides are provided for Java, Kotlin, and XML files to ensure 2-space indentation, as per project standards.

## How to Use

Most modern IDEs and text editors, including Android Studio, have built-in support for `.editorconfig`. Upon opening the project, the editor should automatically detect and apply the rules defined in the `.editorconfig` file. No manual configuration is typically required from the developer.

## References

*   [EditorConfig Official Website](https://editorconfig.org/) [1]
*   [AndroidIDE Ultra CONTRIBUTING.md](https://github.com/Willow7737/AndroidIDE-Ultra/blob/main/CONTRIBUTING.md) [2]

[1]: https://editorconfig.org/
[2]: https://github.com/Willow7737/AndroidIDE-Ultra/blob/main/CONTRIBUTING.md
