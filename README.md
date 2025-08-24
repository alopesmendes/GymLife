![Coverage](https://img.shields.io/codecov/c/github/alopesmendes/GymLife)
![Language](https://img.shields.io/github/languages/top/alopesmendes/GymLife)
![Code size](https://img.shields.io/github/languages/code-size/alopesmendes/GymLife)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/alopesmendes/GymLife)
![GitHub issues](https://img.shields.io/github/issues/alopesmendes/GymLife)
![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/alopesmendes/GymLife/ci.yml)
![GitHub](https://img.shields.io/github/license/alopesmendes/GymLife)
![GitHub last commit](https://img.shields.io/github/last-commit/alopesmendes/GymLife)
![GitHub commit activity](https://img.shields.io/github/commit-activity/m/alopesmendes/GymLife)

This is a Kotlin Multiplatform project targeting Android, iOS, Web, Desktop, Server.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

* `/server` is for the Ktor server application.

* `/shared` is for the code that will be shared between all targets in the project.
  The most important subfolder is `commonMain`. If preferred, you can add code to the platform-specific folders here too.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),
[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform),
[Kotlin/Wasm](https://kotl.in/wasm/)…

We would appreciate your feedback on Compose/Web and Kotlin/Wasm in the public Slack channel [#compose-web](https://slack-chats.kotlinlang.org/c/compose-web).
If you face any issues, please report them on [YouTrack](https://youtrack.jetbrains.com/newIssue?project=CMP).

You can open the web application by running the `:composeApp:wasmJsBrowserDevelopmentRun` Gradle task.

To create a pr with a template can use the command `gh pr create --template <template.md>`
- **🚀 Feature:** [Use feature template](.github/PULL_REQUEST_TEMPLATE/feature.md)
- **🐛 Bug Fix:** [Use bugfix template](.github/PULL_REQUEST_TEMPLATE/bugfix.md)
- **🔥 Hotfix:** [Use hotfix template](.github/PULL_REQUEST_TEMPLATE/hotfix.md)
- **♻️ Refactor:** [Use refactor template](.github/PULL_REQUEST_TEMPLATE/refactor.md)
- **📚 Documentation:** [Use docs template](.github/PULL_REQUEST_TEMPLATE/docs.md)
- **📦 Dependencies:** [Use dependency template](.github/PULL_REQUEST_TEMPLATE/dependency.md)