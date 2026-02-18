#  Expense Tracker App

<div align="center">

![Android](https://img.shields.io/badge/Platform-Android-green.svg)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg)
![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-blue.svg)
![MinSDK](https://img.shields.io/badge/Min%20SDK-24-orange.svg)
![TargetSDK](https://img.shields.io/badge/Target%20SDK-35-brightgreen.svg)


**A modern, feature-rich Android expense tracking application built with Jetpack Compose following MVVM architecture principles.**

[Features](#-features) • [Screenshots](#-screenshots) • [Tech Stack](#-tech-stack) • [Architecture](#-architecture) • [Installation](#-installation) • [Contributing](#-contributing)

</div>

---

##  Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Screenshots](#-screenshots)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Project Structure](#-project-structure)
- [Installation](#-installation)
- [Building](#-building)
- [Database Schema](#-database-schema)
- [Testing](#-testing)
- [Contributing](#-contributing)
- [License](#-license)
- [Contact](#-contact)

---

##  Overview

Expense Tracker App is a comprehensive financial management solution designed to help users track their daily expenses, analyze spending patterns, and gain valuable insights into their financial habits. Built with modern Android development practices, the app leverages Jetpack Compose for a fluid UI experience and follows clean MVVM architecture for maintainability and scalability.

### Key Highlights

-  **Modern UI/UX** - Built with Jetpack Compose and Material Design 3
-  **Rich Analytics** - Interactive charts and visual insights
-  **Real-time Updates** - Reactive UI with Kotlin Flow
-  **Persistent Storage** - Room database for reliable data management
-  **Clean Architecture** - MVVM pattern with separation of concerns
-  **Type-Safe Navigation** - Navigation Compose with argument handling

---

##  Features

### 📊 Expense Management
-  **Add Expenses** - Quick expense entry with intuitive form
-  **Edit & Update** - Modify existing expense records
-  **Swipe to Delete** - Gesture-based deletion for better UX
-  **Smart Categorization** - Organize by Food, Transport, Shopping, Bills, Entertainment, and Other
-  **Payment Tracking** - Support for Cash, Card, and UPI
-  **Detailed Notes** - Add context to each transaction

###  Analytics & Insights
-  **Category Breakdown** - Interactive donut chart showing spending distribution
-  **Trend Analysis** - Weekly spending trends with line charts
-  **Time-based Filtering** - View data by month and year
-  **Smart Summaries** - Total spending, averages, and top categories
-  **Visual Insights** - Color-coded charts for easy understanding

###  User Experience
-  **Onboarding Flow** - Beautiful introduction for new users
-  **Intuitive Dashboard** - Quick overview of financial status
-  **Advanced Filtering** - Filter by category, sort by amount or date
-  **Material Design 3** - Modern, accessible, and beautiful interface
-  **Smooth Animations** - Polished transitions and micro-interactions
-  **Responsive Design** - Optimized for different screen sizes

###  Data Management
-  **Local Storage** - All data stored securely on device
-  **Real-time Sync** - Instant UI updates with Flow
-  **Efficient Queries** - Optimized Room database operations
-  **Data Privacy** - No external data sharing

---



##  Tech Stack

### Core Technologies

| Category | Technology |
|----------|-----------|
| **Language** | Kotlin 2.0.21 |
| **UI Framework** | Jetpack Compose |
| **Architecture** | MVVM (Model-View-ViewModel) |
| **Database** | Room 2.6.1 |
| **Async** | Coroutines & Flow |
| **Navigation** | Navigation Compose 2.9.7 |
| **DI** | Manual Dependency Injection |
| **Build System** | Gradle 8.11.1 with Version Catalog |

### Key Libraries

```kotlin
// UI & Design
- Jetpack Compose BOM 2024.09.00
- Material Design 3
- Material Icons Extended
- MPAndroidChart v3.1.0

// Architecture Components
- Room Database 2.6.1
- ViewModel & LiveData
- Navigation Compose 2.9.7

// Development
- Kotlin Coroutines
- Kotlin Flow
- KSP (Kotlin Symbol Processing)
```

### Development Environment
- **IDE**: Android Studio Koala | 2024.1.1+
- **JDK**: Java 11+
- **Min SDK**: 24 (Android 7.0 Nougat)
- **Target SDK**: 35 (Android 15)
- **Compile SDK**: 35

---

##  Architecture

### MVVM Pattern

The app follows a strict **Model-View-ViewModel (MVVM)** architectural pattern for clean separation of concerns:

```
┌─────────────────────────────────────────────────────────┐
│                         VIEW                            │
│              (Jetpack Compose Screens)                  │
│  • Observes ViewModel state via StateFlow              │
│  • Renders UI based on state                           │
│  • Sends user actions to ViewModel                     │
└────────────────────┬────────────────────────────────────┘
                     │
                     ↓
┌─────────────────────────────────────────────────────────┐
│                      VIEWMODEL                          │
│         (Business Logic & State Management)             │
│  • Exposes UI state via StateFlow                      │
│  • Handles user actions                                │
│  • Coordinates with Repository/DAO                     │
│  • Survives configuration changes                      │
└────────────────────┬────────────────────────────────────┘
                     │
                     ↓
┌─────────────────────────────────────────────────────────┐
│                        MODEL                            │
│              (Data Layer & Business Logic)              │
│  • Room Database entities                              │
│  • DAO interfaces                                      │
│  • Data classes                                        │
│  • Database operations                                 │
└─────────────────────────────────────────────────────────┘
```

### ViewModels

| ViewModel | Responsibility |
|-----------|---------------|
| `DashboardViewModel` | Manages dashboard state, today/month totals, recent expenses |
| `AddExpenseViewModel` | Handles expense creation and validation |
| `EditExpenseViewModel` | Manages expense editing and deletion |
| `AnalyticsViewModel` | Provides analytics data with time filtering |
| `ExpenseListViewModel` | Manages expense list, filtering, and sorting |
| `ExpenseDetailViewModel` | Handles individual expense details |

### Data Flow

```kotlin
User Action → Composable → ViewModel → DAO → Room Database
                                    ↓
User sees update ← Composable ← StateFlow ← Flow ← Room Database
```

---

## 📁 Project Structure

```
ExpenseTrackerApp/
├── app/
│   ├── build.gradle.kts
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/example/expensetrackerapp/
│       │   │   ├── Database.kt                    # Room DB, entities, DAOs
│       │   │   ├── MainActivity.kt                # Navigation host
│       │   │   ├── ui/
│       │   │   │   └── theme/                     # Material 3 theming
│       │   │   │       ├── Color.kt
│       │   │   │       ├── Theme.kt
│       │   │   │       └── Type.kt
│       │   │   └── uiscreens/
│       │   │       └── addexpense/
│       │   │           ├── AddExpenseScreen.kt
│       │   │           ├── AddExpenseViewModel.kt
│       │   │           ├── OnBoarding/
│       │   │           │   └── OnboardingScreen1.kt
│       │   │           ├── EditExpense/
│       │   │           │   ├── EditExpenseScreen.kt
│       │   │           │   └── EditExpenseViewModel.kt
│       │   │           ├── dashboard/
│       │   │           │   ├── DashboardScreen.kt
│       │   │           │   └── DashboardViewModel.kt
│       │   │           ├── expenselists/
│       │   │           │   ├── ExpenseListsScreen.kt
│       │   │           │   ├── ExpenseListViewModel.kt
│       │   │           │   ├── ExpenseDetailScreen.kt
│       │   │           │   └── ExpenseDetailViewModel.kt
│       │   │           └── analytics/
│       │   │               ├── AnalyticsScreen.kt
│       │   │               └── AnalyticsViewModel.kt
│       │   └── res/                               # Resources
│       ├── androidTest/                           # Instrumented tests
│       └── test/                                  # Unit tests
├── gradle/
│   └── libs.versions.toml                         # Version catalog
├── build.gradle.kts
├── settings.gradle.kts
├── .gitignore
└── README.md
```

---

##  Installation

### Prerequisites

Before you begin, ensure you have the following installed:

-  **Android Studio** Koala (2024.1.1) or later
-  **JDK 11** or higher
-  **Android SDK 24+** (Android 7.0 Nougat or higher)
-  **Git** for version control

### Setup Instructions

1. **Clone the Repository**

```bash
git clone https://github.com/yourusername/ExpenseTrackerApp.git
cd ExpenseTrackerApp
```

2. **Open in Android Studio**

- Launch Android Studio
- Select **"Open an Existing Project"**
- Navigate to the cloned directory and click **"OK"**

3. **Sync Project with Gradle Files**

Android Studio will automatically:
- Download required dependencies
- Configure build tools
- Index project files

Wait for the sync to complete (check the progress bar at the bottom).

4. **Configure Android SDK**

Ensure you have the following installed via **SDK Manager**:
- Android SDK Platform 35
- Android SDK Build-Tools 35.x.x
- Android Emulator (if you don't have a physical device)

5. **Run the Application**

**Option A: Using Emulator**
- Open **AVD Manager** (Tools → Device Manager)
- Create or start an existing emulator
- Click the **Run** button  or press `Shift + F10`

**Option B: Using Physical Device**
- Enable **Developer Options** on your Android device
- Enable **USB Debugging**
- Connect device via USB
- Select your device from the device dropdown
- Click the **Run** button 

---

##  Building

### Debug Build

```bash
# Build debug APK
./gradlew assembleDebug

# Output location
# app/build/outputs/apk/debug/app-debug.apk
```

### Release Build

```bash
# Build release APK (unsigned)
./gradlew assembleRelease

# Build signed release
./gradlew assembleRelease -Pandroid.injected.signing.store.file=your-keystore.jks \
  -Pandroid.injected.signing.store.password=your-store-password \
  -Pandroid.injected.signing.key.alias=your-key-alias \
  -Pandroid.injected.signing.key.password=your-key-password
```

### Running Tests

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Generate test coverage report
./gradlew jacocoTestReport
```

### Code Quality

```bash
# Run lint checks
./gradlew lint

# Format code with ktlint
./gradlew ktlintFormat
```

---

##  Database Schema

### Expense Entity

```kotlin
@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double,
    val category: String,
    val date: Long,              // Unix timestamp
    val paymentMethod: String,
    val note: String
)
```

### Supporting Data Classes

```kotlin
data class CategoryTotal(
    val category: String,
    val total: Double
)

data class DailyTotal(
    val date: Long,
    val total: Double
)

data class WeeklyTotal(
    val week: Int,
    val total: Double
)
```

### Key Queries

| Query | Purpose |
|-------|---------|
| `getAllExpenses()` | Retrieve all expenses ordered by date |
| `getCategoryTotals(month, year)` | Aggregate spending by category |
| `getDailyTotals(month, year)` | Daily spending breakdown |
| `getWeeklyTotals(month, year)` | Weekly spending trends |
| `insertExpense(expense)` | Add new expense |
| `updateExpense(expense)` | Modify existing expense |
| `deleteExpense(expense)` | Remove expense |

---

##  Testing

### Unit Tests

Located in `app/src/test/`:
- ViewModel logic tests
- Utility function tests
- Data transformation tests

### Instrumented Tests

Located in `app/src/androidTest/`:
- UI component tests
- Database tests
- Navigation tests

### Running Tests

```bash
# All unit tests
./gradlew test

# Specific test class
./gradlew test --tests "com.example.expensetrackerapp.DashboardViewModelTest"

# All instrumented tests
./gradlew connectedAndroidTest
```

---

##  Configuration

### Version Catalog (gradle/libs.versions.toml)

```toml
[versions]
agp = "8.9.2"
kotlin = "2.0.21"
room = "2.6.1"
compose-bom = "2024.09.00"
navigation = "2.9.7"

[libraries]
androidx-room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
androidx-room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "compose-bom" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
```

---

##  Contributing

Contributions are welcome and appreciated! Here's how you can contribute:

### Getting Started

1. **Fork the Repository**
   - Click the "Fork" button at the top right of this page

2. **Clone Your Fork**
   ```bash
   git clone https://github.com/your-username/ExpenseTrackerApp.git
   cd ExpenseTrackerApp
   ```

3. **Create a Feature Branch**
   ```bash
   git checkout -b feature/amazing-feature
   ```

4. **Make Your Changes**
   - Follow the coding guidelines below
   - Write meaningful commit messages
   - Add tests for new features

5. **Commit Your Changes**
   ```bash
   git commit -m "Add: Amazing feature description"
   ```

6. **Push to Your Fork**
   ```bash
   git push origin feature/amazing-feature
   ```

7. **Open a Pull Request**
   - Go to the original repository
   - Click "New Pull Request"
   - Provide a clear description of your changes



### Commit Message Convention

```
Type: Brief description

Detailed description (optional)

Types: Add, Update, Fix, Remove, Refactor, Style, Test, Docs
```

**Examples:**
```
Add: Category filter to expense list
Fix: Crash when deleting last expense
Update: Analytics chart color scheme
```










