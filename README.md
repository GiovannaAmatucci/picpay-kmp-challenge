# 📱 PicPay Challenge - Kotlin Multiplatform (KMP)

![Kotlin](https://img.shields.io/badge/Kotlin-2.3%2B-7f52ff?logo=kotlin&logoColor=white)
![KMP](https://img.shields.io/badge/Kotlin_Multiplatform-Enabled-7f52ff?logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-MinSdk%2027-3DDC84?logo=android&logoColor=white)
![iOS](https://img.shields.io/badge/iOS-14.0%2B-000000?logo=apple&logoColor=white)
![CI Status](https://github.com/GiovannaAmatucci/picpay-kmp-challenge/actions/workflows/ci.yml/badge.svg)

> **Note:** This project is a **KMP migration** of the original Native Android project.
> You can compare the evolution with the legacy repository here: [Original Android Native Repository](https://github.com/GiovannaAmatucci/picpay-android-challenge)

---

## 🚀 About the Project

This project takes an existing modern Android app (already built with Compose, Koin, and Ktor) and refactors it into a fully **Kotlin Multiplatform (KMP)** architecture.

The main goal was to demonstrate **code sharing capabilities**: moving the existing logic—which was locked inside the Android module—to a shared environment that runs natively on both **Android** and **iOS**.

### 🎯 What changed? (Architecture Evolution)

| Feature | 🤖 Original Project (Native) | 🚀 KMP Project (Current) |
| :--- | :--- | :--- |
| **Scope** | Android Only | **Android & iOS (Shared)** |
| **Architecture** | Android-Specific Clean Arch | **Multiplatform Clean Arch** |
| **UI** | Jetpack Compose (Android) | **Compose Multiplatform (Android + iOS)** |
| **Networking** | Ktor (Android Engine) | **Ktor (Cross-Platform Engines)** |
| **Dependency Inj.** | Koin (Android Context) | **Koin (Platform-Agnostic Modules)** |
| **Database** | Room (Android-locked) | **Room Multiplatform (SQLite Bundled)** |
| **Tests** | JUnit4 (JVM) | **Kotlin Test (Common)** |

---

## 🛠 Tech Stack

The project leverages the most modern Kotlin ecosystem, unified across platforms:

* **Language:** [Kotlin 2.3+](https://kotlinlang.org/)
* **Core:** [Kotlin Multiplatform](https://kotlinlang.org/lp/multiplatform/)
* **UI:** [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/) (Material 3)
* **Navigation:** AndroidX Navigation Compose (KMP support)
* **Networking:** [Ktor Client](https://ktor.io/)
* **DI:** [Koin](https://insert-koin.io/)
* **Database:** [Room KMP](https://developer.android.com/kotlin/multiplatform/room)
* **Image Loading:** [Coil 3.0](https://coil-kt.github.io/coil/)
* **Configuration:** [BuildKonfig](https://github.com/codingfeline/buildkonfig)

### 🏗 Folder Structure

```text
commonMain/
├── data/           # Repositories, Ktor, Room DAO (Shared)
├── domain/         # UseCases, Models, and Interfaces (Pure Kotlin)
├── presentation/   # ViewModels and Compose UI (Shared)
└── di/             # Koin Modules
androidMain/        # Android-specific configurations (Context, Activities)
iosMain/            # iOS-specific configurations (MainViewController)
```

## 🧪 Quality & Assurance

The test suite was refactored to ensure the shared logic works flawlessly on non-JVM environments (like iOS):

* **Unit Tests:** 100% migrated to `commonTest`.
* **Strategy:** Replaced reflection-based Mocks (MockK) with **Manual Fakes** to ensure compatibility with the Kotlin Native memory model and runtime.
* **CI/CD:** GitHub Actions pipeline configured to validate:
    * Android Build.
    * Test execution on **iOS Simulator** (Apple Silicon).

---

## 🚀 How to Run

### Prerequisites
* Android Studio (Ladybug or newer).
* JDK 17 or 21.
* (Optional) Xcode to run the iOS app locally.

### 🤖 Android
1.  Open the project in Android Studio.
2.  Sync Gradle.
3.  Select the `composeApp` configuration and run it on an emulator.

Or via terminal:
```bash
./gradlew installDebug
```
### 🍎 iOS

**Option A (With Mac + Xcode):**
1. Open the `iosApp.xcodeproj` file in Xcode.
2. Run on a Simulator.

**Option B (No Mac - Validation via Tests):**
You can run the iOS simulated tests via terminal:
```bash
./gradlew iosSimulatorArm64Test
```

### 🛡️ Security
* **URL Sanitization:** The HttpClient includes protection against URL configuration errors (`.trim().replace`).

---
Developed by **Giovanna Amatucci** 👩‍💻

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/giovanna-amatucci2001/)
