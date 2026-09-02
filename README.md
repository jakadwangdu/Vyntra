# ⚡ Vyntra

> **Minimalist, High-Performance AI Nutrition Intelligence, Food Vision & Fitness Coaching for Android.**  
> Powered by **Gemini 3.5 Flash**, **Jetpack Compose (Material 3)**, and an **Offline-First Room Database**.

---

<!-- Badges -->
[![Download Latest APK](https://img.shields.io/badge/Download-Vyntra.apk-0E0F12?style=for-the-badge&logo=android&logoColor=white)](https://github.com/skituspanda/Vyntra/releases/latest/download/Vyntra.apk)
[![GitHub Release](https://img.shields.io/badge/Release-v1.0.0-orange?style=for-the-badge&logo=github)](https://github.com/skituspanda/Vyntra/releases/latest)
[![Platform](https://img.shields.io/badge/Platform-Android%208.0%2B%20(API%2024%2B)-007AFF?style=for-the-badge&logo=android)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-7F52FF?style=for-the-badge&logo=kotlin)](https://kotlinlang.org)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue?style=for-the-badge)](LICENSE)

---

## 🚀 Download & Ready to Use

Install **Vyntra** straight onto your Android smartphone or tablet without needing Android Studio or compiling code!

### 📥 Direct Download Links

| File | Direct Link | Description |
| :--- | :--- | :--- |
| **Vyntra APK** | [👉 **Download Vyntra.apk**](https://github.com/skituspanda/Vyntra/releases/latest/download/Vyntra.apk) | Latest pre-compiled APK (Ready to install) |
| **GitHub Releases** | [🏷️ **Browse All Releases**](https://github.com/skituspanda/Vyntra/releases) | Release notes, changelog, and historical versions |

---

### 📲 How to Install on Android

1. **Download APK**: Tap [**Download Vyntra.apk**](https://github.com/skituspanda/Vyntra/releases/latest/download/Vyntra.apk) directly from your mobile browser.
2. **Enable Unknown Sources (If Prompted)**:
   - When the download completes and you open the `.apk`, Android may display *"For your security, your phone is not allowed to install unknown apps from this source"*.
   - Tap **Settings** in the popup dialog and toggle **Allow from this source**.
3. **Install**: Tap **Install** on the system confirmation sheet.
4. **Launch**: Open **Vyntra** from your home screen or app drawer!

> **Note**: The APK is signed with a standard Android certificate. You may see a Google Play Protect prompt asking to verify; simply tap **Install anyway**.

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

## 🛠️ GitHub Repository & Release Workflow Setup

This repository is pre-configured with a **GitHub Actions CI/CD workflow** (`.github/workflows/release.yml`) that automatically builds and publishes the APK whenever you release a new version.

### Option A: Automatic Release via Git Tag (Recommended)

When you push this repository to your GitHub account (`https://github.com/<YOUR_USERNAME>/<YOUR_REPO>`):

1. **Tag your commit**:
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```
2. **Watch the Build**: Go to the **Actions** tab on your GitHub repository. The `Build & Release Android APK` workflow will automatically:
   - Set up Java 17 and Gradle.
   - Build the APK.
   - Create a new GitHub Release with the tag `v1.0.0`.
   - Attach `Vyntra.apk` ready for direct download.

### Option B: Trigger Build Manually via GitHub UI

1. Go to your GitHub repository.
2. Click on the **Actions** tab.
3. Select **Build & Release Android APK** from the sidebar.
4. Click **Run workflow**, enter the version name (e.g. `v1.0.0`), and press the green button.
5. Once complete, your new release and download link are live!

### Option C: Manual Upload to GitHub Releases

If you prefer to build locally and upload manually:
1. Build the APK locally:
   ```bash
   gradle assembleDebug
   ```
2. Locate the APK at:
   `app/build/outputs/apk/debug/app-debug.apk`
3. Rename it to `Vyntra.apk`.
4. In your GitHub repository, go to **Releases** > **Draft a new release**.
5. Set the tag (e.g. `v1.0.0`), give it a title, and drag-and-drop `Vyntra.apk` into the release binary drop zone.
6. Click **Publish release**.

> 💡 **Tip**: Update the URL in the badges and download links at the top of this `README.md` to match your own GitHub username if different from `skituspanda`.

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
5. Click **Add secret**. The workflow will automatically inject it into the built APK.

---

## 💻 Building from Source

### Prerequisites
- **Android Studio** Ladybug (2024.2.1+) or newer
- **JDK 17** or newer
- **Android SDK Platform 36** (API 36)

### Build Commands (Terminal)
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

## 🏗️ Architecture & Tech Stack

- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material Design 3 (M3)
- **Language**: [Kotlin 2.0](https://kotlinlang.org/)
- **Architecture**: Clean MVVM (Model-View-ViewModel) + Single Source of Truth Flows
- **AI Vision & Chat**: Google Gemini 3.5 Flash via Generative AI / Retrofit REST interface
- **Local Storage**: [Room Database](https://developer.android.com/training/data-storage/room) with SQLite & Kotlin Coroutines Flow
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/)
- **Async Concurrency**: Kotlin Coroutines & StateFlow / SharedFlow
- **Serialization**: Moshi Kotlin Codegen & Kotlinx Serialization

---

## 📄 License

This project is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for details.
