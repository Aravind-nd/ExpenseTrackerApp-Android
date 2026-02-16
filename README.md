# ExpenseTrackerApp-Android

An Android expense tracking app built with **Jetpack Compose** for UI, **Room** for local persistence, and **MVVM** architecture. The project is a standard **Gradle (Kotlin DSL)** Android application (primary `app` module) intended to be opened and run in **Android Studio**.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture (MVVM)](#architecture-mvvm)
- [Data Layer (Room)](#data-layer-room)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Clone the repository](#clone-the-repository)
  - [Open in Android Studio](#open-in-android-studio)
  - [Build & Run](#build--run)
- [Usage Guidelines](#usage-guidelines)
- [Project Structure](#project-structure)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [License](#license)

---

## Project Overview

**ExpenseTrackerApp-Android** helps you record day-to-day expenses and review spending history. It uses a modern Android stack (Compose + Room + MVVM) and is designed to be easy to extend with additional features like charts, budgets, export, or cloud sync.

---

## Features

Common app capabilities (some may depend on what’s implemented in this repo):

- Add, edit, and delete expense entries
- Categorize expenses (e.g., Food, Travel, Bills)
- Add optional notes/descriptions
- View a list of expenses and basic summaries (by day/week/month) *(if implemented)*
- Filter/search by category/date/amount *(if implemented)*

---

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose
- **Architecture:** MVVM (ViewModel-driven UI state)
- **Local database:** Room (SQLite)
- **Build system:** Gradle (Kotlin DSL) — `build.gradle.kts`, `settings.gradle.kts`
- **IDE:** Android Studio

---

## Architecture (MVVM)

The app follows **Model–View–ViewModel**:

- **View (UI layer):** Jetpack Compose screens and composables
  - Observes state exposed by ViewModels
  - Sends user events (button clicks, text input) to ViewModels
- **ViewModel (presentation layer):**
  - Holds UI state (e.g., expense list, loading/error state)
  - Runs business logic for user actions (add/update/delete)
  - Calls into repositories/use-cases to access data
- **Model (data/domain layer):**
  - Repository provides a clean API to the rest of the app
  - Room entities/DAO handle persistence

A typical flow:

1. Compose screen triggers an event → `ViewModel`
2. `ViewModel` calls `Repository`
3. `Repository` reads/writes via `Room DAO`
4. Updates are observed back in UI (often via `Flow` / `LiveData`)

---

## Data Layer (Room)

Room is used for local storage:

- **Entity:** represents a table row (e.g., `ExpenseEntity`)
- **DAO:** defines queries and database operations (`@Insert`, `@Query`, `@Delete`, etc.)
- **Database:** the Room database class that ties entities + DAOs together

Room typically works best with reactive streams:

- Use `Flow<List<ExpenseEntity>>` from DAO so the UI updates automatically when the DB changes.

---

## Getting Started

### Prerequisites

- Android Studio (latest stable recommended)
- Android SDK installed (Android Studio → SDK Manager)
- Emulator or a physical Android device

### Clone the repository

```bash
git clone https://github.com/Aravind-nd/ExpenseTrackerApp-Android.git
cd ExpenseTrackerApp-Android
```

### Open in Android Studio

1. Open **Android Studio**
2. Select **Open**
3. Choose the project folder: `ExpenseTrackerApp-Android`
4. Wait for **Gradle Sync** to complete

### Build & Run

From Android Studio:

1. Choose a device (emulator/phone)
2. Press **Run** (▶)

From the command line:

```bash
# macOS / Linux
./gradlew assembleDebug

# Windows
gradlew.bat assembleDebug
```

---

## Usage Guidelines

Typical usage:

1. Launch the app
2. Add an expense (amount, category, date, optional note)
3. Save the entry
4. Review the expense list and summaries
5. Edit/delete entries as needed

---

## Project Structure

Top-level structure:

```text
ExpenseTrackerApp-Android/
├─ app/                    # Main Android application module (Compose + MVVM + Room)
├─ gradle/                 # Gradle wrapper support files
├─ .idea/                  # IDE settings
├─ .gitignore
├─ build.gradle.kts        # Root build script (Kotlin DSL)
├─ settings.gradle.kts     # Module includes
├─ gradle.properties       # Gradle configuration
├─ gradlew
└─ gradlew.bat
```

Common structure inside `app/src/main` (may vary):

```text
app/src/main/
├─ AndroidManifest.xml
├─ kotlin/ (or java/)
│  └─ <your.package.name>/
│     ├─ ui/               # Compose screens, components, navigation
│     ├─ viewmodel/        # ViewModels + UI state holders
│     ├─ data/
│     │  ├─ local/         # Room: entities, dao, database
│     │  └─ repository/    # Repository implementations
│     ├─ domain/           # Models / use-cases (optional but recommended)
│     └─ di/               # Dependency injection setup (if used)
└─ res/                    # App icons, themes, etc. (Compose apps still use res/)
```

---

## Troubleshooting

- **Gradle sync/build fails**
  - *File → Invalidate Caches / Restart*
  - Ensure Android Studio is using a compatible JDK (usually the bundled one)
- **Room schema / migration errors**
  - If schema changes, add a proper migration or (for dev only) clear app data/uninstall
- **Emulator/device issues**
  - Update SDK tools and emulator images via SDK Manager

---

## Contributing

1. Fork the repo
2. Create a branch: `feature/your-change`
3. Commit with a clear message
4. Open a Pull Request with details and screenshots if UI changes are involved

---

