# Contributing

This document outlines how to propose a change to AndroidIDE Ultra. By contributing to this project, you
agree to abide the terms specified in the [CODE OF CONDUCT](./CODE_OF_CONDUCT.md).

## Requirements

- **Android Studio Jellyfish** or newer.
- **JDK 17** or **JDK 21** (JDK 17 is recommended for stability).

> [!NOTE]
> AndroidIDE Ultra is compatible with modern Android Gradle Plugin (AGP) 8.5+. Ensure your environment is up to date to avoid build synchronization issues.

## Build the project

Open the project in Android Studio. No extra steps are needed for this.

It is possible to build AndroidIDE Ultra in AndroidIDE Ultra itself. But, as it might be a too heavy task for an
Android device to be able to build the project, Android Studio is always recommended.

You can refer to the [AndroidIDE Ultra Documentation](https://github.com/Willow7737/AndroidIDE-Ultra) for getting started with development. 

## Source code format

- Indents : 2-space
- Java : `GoogleStyle`. Either use `google-java-format` or
  import [this](https://raw.githubusercontent.com/google/styleguide/gh-pages/intellij-java-google-style.xml)
  code style.
- Kotlin: Use [`ktfmt`](https://plugins.jetbrains.com/plugin/14912-ktfmt) IntelliJ Plugin and set
  the code style to `Google (internal)`
  . [`Learn more`](https://github.com/facebook/ktfmt#intellij-android-studio-and-other-jetbrains-ides)
  .
- XML : Default Android Studio formatter with 2-space indentations.

## Propose a change

Before proposing a change, it is always recommended to open an issue and discuss it with the
maintainers.

**The `main` branch is protected and requires all the commits to be signed with your GPG key and the
commit history to be linear.**
See [protected branches](https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/defining-the-mergeability-of-pull-requests/about-protected-branches)
.

To contribute to this project,

- Fork the repo.
- Clone the forked repo to your local machine.
- Open the project.
- Make your changes.
- Create a pull request. Give a proper title and description to your PR.

## Issues & Pull requests

Use the issue/PR templates whenever possible. Provide a proper title and a clear description of the
issue/PR. For bug reports, provide a step-by-step procedure to reproduce the issue. Always put
things like crash
logs, and build outputs in `code blocks`.

## Contact us

If you want to discuss anything about the project, you can discuss it in
the [Telegram group](https://t.me/androidide_discussions).
