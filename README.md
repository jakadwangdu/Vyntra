# ⚡ Vyntra

> **Minimalist, High-Performance AI Nutrition Intelligence, Food Vision & Fitness Coaching for Android.**  
> Powered by **Gemini 3.5 Flash**, **Jetpack Compose (Material 3)**, and an **Offline-First Room Database**.

---

<!-- Badges -->
[![Download Latest APK](https://img.shields.io/badge/Download-Vyntra.apk-0E0F12?style=for-the-badge&logo=android&logoColor=white)](https://github.com/skituspanda/Vyntra/releases/latest/download/Vyntra.apk)
[![GitHub Actions CI/CD](https://img.shields.io/badge/Actions-Trigger%20Build-2ea44f?style=for-the-badge&logo=githubactions&logoColor=white)](https://github.com/skituspanda/Vyntra/actions)
[![GitHub Packages](https://img.shields.io/badge/Packages-GHCR%20Docker-007AFF?style=for-the-badge&logo=docker&logoColor=white)](https://github.com/skituspanda/Vyntra/pkgs/container/vyntra-apk)
[![GitHub Release](https://img.shields.io/badge/Release-v1.0.0-orange?style=for-the-badge&logo=github)](https://github.com/skituspanda/Vyntra/releases)
[![Platform](https://img.shields.io/badge/Platform-Android%208.0%2B%20(API%2024%2B)-007AFF?style=for-the-badge&logo=android)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-7F52FF?style=for-the-badge&logo=kotlin)](https://kotlinlang.org)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue?style=for-the-badge)](LICENSE)

---

## 🚀 How to Download the App via GitHub

Vyntra supports three primary ways to download and install via GitHub, automated entirely by **GitHub Actions**:

```
                  ┌─────────────────────────────────────────┐
                  │          Trigger GitHub Action          │
                  │  (Push to main / Git Tag / Web UI / CLI)│
                  └────────────────────┬────────────────────┘
                                       │
                         Builds Vyntra.apk on Runner
                                       │
            ┌──────────────────────────┼──────────────────────────┐
            ▼                          ▼                          ▼
 ┌──────────────────────┐  ┌──────────────────────┐  ┌──────────────────────┐
 │   GitHub Releases    │  │  GitHub Actions Runs │  │   GitHub Packages    │
 │ (Direct APK Download)│  │ (Download Artifacts) │  │(GHCR Container Image)│
 └──────────────────────┘  └──────────────────────┘  └──────────────────────┘
```

---

### 1️⃣ Method 1: Download via GitHub Releases (Direct APK)

The fastest and most direct way for Android phones and tablets:

| Resource | Direct Link | Description |
| :--- | :--- | :--- |
| **Latest APK** | [👉 **Download Vyntra.apk**](https://github.com/skituspanda/Vyntra/releases/latest/download/Vyntra.apk) | Standalone APK file, ready to install |
| **All Releases** | [🏷️ **Browse GitHub Releases**](https://github.com/skituspanda/Vyntra/releases) | Changelog, release notes, and version history |

> *Note: If this is a newly created repository, the direct release link activates once you trigger the initial GitHub Actions build or push your first git tag!*

---

### 2️⃣ Method 2: Download via GitHub Actions (Trigger & Download Artifacts)

Every time GitHub Actions runs, it compiles the APK and stores it as a downloadable **Workflow Artifact** (`Vyntra-APK`):

#### How to Trigger the Build:
1. Go to your repository on GitHub: `https://github.com/skituspanda/Vyntra/actions`.
2. In the left sidebar, click **Build, Release & Publish Vyntra APK**.
3. Click the **Run workflow** dropdown on the right:
   - Version tag: `v1.0.0` (or your custom version)
   - Publish to GitHub Releases: `true`
   - Publish to GitHub Packages: `true`
4. Click the green **Run workflow** button.

#### How to Download the Artifact:
1. Click on the completed workflow run.
2. Scroll down to the **Artifacts** section at the bottom of the summary page.
3. Click **Vyntra-APK** to download the freshly compiled binary immediately!

#### Triggering via GitHub CLI (`gh`):
```bash
gh workflow run release.yml -f version_name=v1.0.0
```

---

### 3️⃣ Method 3: Download via GitHub Packages (GHCR Container Registry)

The APK is also packaged and published to **GitHub Packages** (`ghcr.io`):

```bash
# Pull the package container
docker pull ghcr.io/skituspanda/vyntra/vyntra-apk:latest

# Extract the Vyntra.apk from the container
docker create --name vyntra-temp ghcr.io/skituspanda/vyntra/vyntra-apk:latest
docker cp vyntra-temp:/dist/Vyntra.apk ./Vyntra.apk
docker rm vyntra-temp
```

Direct package URL: [https://github.com/skituspanda/Vyntra/pkgs/container/vyntra-apk](https://github.com/skituspanda/Vyntra/pkgs/container/vyntra-apk)

---

### 📱 In-App Download Feature

You can also trigger downloads and copy GitHub links directly inside the app:
- Tap the **Cloud Download** icon in the top header of the Dashboard.
- Or tap the **"Get Vyntra APK"** banner on the main screen.
- An interactive bottom sheet allows you to:
  - Download the APK directly.
  - Open GitHub Actions to trigger a build.
  - View GitHub Packages.
  - One-tap copy the direct download URL, GitHub CLI commands, and Docker pull commands.

---

### 📲 How to Install on Android

1. **Download `Vyntra.apk`** to your phone using any of the methods above.
2. **Allow Installation (If Prompted)**:
   - If Android shows *"For your security, your phone is not allowed to install unknown apps from this source"*, tap **Settings** and enable **Allow from this source**.
3. **Install**: Tap **Install** on the system package installer prompt.
4. **Launch**: Open **Vyntra** from your home screen or app drawer!

---

## ✨ Features at a Glance

- **📸 AI Food Vision Scanner**:
  - Point your camera or choose a photo from your gallery using Android Photo Picker.
  - Multimodal Gemini 3.5 Flash integration detects dishes, estimates calories, calculates macronutrients (carbs, protein, fats), and identifies vitamins and minerals.
  - Interactive viewfinder with framing brackets, dashed alignment lines, and one-tap preset foods (*Panna Cotta, Quinoa Veggie Bowl, Grilled Salmon, Açaí Bowl*).

- **⚖️ Dynamic Portion Calibration**:
  - Live portion stepper (from 0.25x up to 4x serving size) with immediate, proportional recalculation of calories and macronutrients.
  - Visual ingredient tag bubbles and micronutrient breakdown chips.
  - Meal categorization (*Breakfast, Lunch, Dinner, Snacks*) with 1-tap logging into your diary.

- **📊 Comprehensive Daily Dashboard**:
  - **Calorie Arc Gauge**: Animated circular progress displaying remaining calories, calories eaten, and calories burned.
  - **Macro Tracking**: Real-time progress bars for Carbohydrates, Protein, and Fats against customized daily targets.
  - **Weekly Date Selector**: Seamlessly jump between past and current dates.
  - **Hydration Tracker**: 8-glass visual water tracker measuring progress toward the 64 fl oz daily goal.

- **🏋️ Personalized Fitness & Workout Coaching**:
  - Tailored training routines for **Bulking (Hypertrophy Focus)** and **Cutting (Metabolic HIIT)**.
  - Exercise guides with proper form execution cues, safety tips, and estimated calories burned.
  - Complete workouts with one tap to automatically deduct burned calories on your dashboard.

- **💬 Conversational AI Nutrition Coach**:
  - Integrated nutrition and fitness assistant aware of your daily calorie balance, macro targets, and logged meals.
  - Quick-prompt chips for instant nutrition advice, post-workout recovery tips, and high-protein snack ideas.

- **📴 Offline-First Room Architecture**:
  - Built with Android Room (SQLite). All your meals, water intake, user profile goals, and workout history stay private and stored locally on your device.

---

## 🛠️ GitHub Actions Workflow Configuration

The workflow file is located at [`.github/workflows/release.yml`](.github/workflows/release.yml).

### Workflow Triggers:
- **`push` to branches**: `main`, `master`
- **`push` with tags**: `v*` (e.g. `v1.0.0`)
- **`workflow_dispatch`**: Manual one-click trigger in the GitHub Actions tab

### Automated Steps:
1. Sets up JDK 17 and Gradle with intelligent caching.
2. Injects `GEMINI_API_KEY` from GitHub Repository Secrets into `.env`.
3. Runs `./gradlew assembleDebug --stacktrace`.
4. Uploads `Vyntra.apk` as a **GitHub Actions Artifact** (stored for 30 days).
5. Creates a **GitHub Release** with auto-generated release notes and attaches `Vyntra.apk`.
6. Publishes an OCI container image containing `Vyntra.apk` to **GitHub Packages (GHCR)**.

---

## 🔑 Environment Variables & API Key Setup

Vyntra uses **Gemini 3.5 Flash** for its AI food vision and conversational coaching.

### For Local Development:
1. Copy the example environment file:
   ```bash
   cp .env.example .env
   ```
2. Open `.env` and insert your Gemini API Key:
   ```properties
   GEMINI_API_KEY=AIzaSy...your_gemini_api_key_here
   ```
   *(You can get a free key from [Google AI Studio](https://aistudio.google.com/app/apikey))*

### For GitHub Actions Automated Builds:
1. In your GitHub repository, navigate to **Settings** > **Secrets and variables** > **Actions**.
2. Click **New repository secret**.
3. Name: `GEMINI_API_KEY`
4. Value: `your_gemini_api_key`
5. Click **Add secret**.

---

## 💻 Building Locally from Source

### Prerequisites
- **Android Studio** Ladybug (2024.2.1+) or newer
- **JDK 17** or newer
- **Android SDK Platform 36** (API 36)

### Build Commands
```bash
# Clone the repository
git clone https://github.com/skituspanda/Vyntra.git
cd Vyntra

# Create your .env file
cp .env.example .env

# Build debug APK
gradle assembleDebug

# Run local unit tests
gradle :app:testDebugUnitTest
```

---

## 📄 License

This project is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for details.
