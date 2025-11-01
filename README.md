# 🩺 IntelliSoft Patient Health Tracker

A modern Android app built with **Jetpack Compose**, **MVVM**, and **Kotlin**, designed to simplify patient management — including recording vitals, computing BMI automatically, and guiding users through personalized health assessments.

---

## 📱 Features

- 🧍‍♀️ Patient Registration and Listing  
- 📊 Add and track patient vitals (Height, Weight, BMI)  
- 🧠 Intelligent Assessment Flow (General or Overweight)  
- 📅 Integrated Date Picker for visit dates  
- 💾 Offline-first architecture with Room  
- 🌐 Secure API communication with Token-based Auth  
- 🎨 Modern UI built using Material 3 and Compose  

---

## 🏗️ Tech Stack

| Layer | Technologies |
|-------|---------------|
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM, Manual Dependency Injection |
| Networking | Retrofit, OkHttp |
| Local Storage | Room Database |
| Async Tasks | Kotlin Coroutines, Flow |
| Auth | TokenManager (SharedPreferences) |
| Navigation | Jetpack Navigation Component |
| Background Tasks | WorkManager |

---

## 🧭 App Flow

Login → Patient List → Add Vitals → Assessment (General / Overweight) → Summary


---

## 🖼️ Screenshots

| Onboarding | Patient List | Add Vitals | Assessment |
|-------------|--------------|-------------|-------------|
| ![Onboarding](screenshots/onboarding.png) | ![Patient List](screenshots/patient_list.png) | ![Vitals](screenshots/vitals_form.png) | ![Assessment](screenshots/assessment.png) |

> 📝 Place screenshots in a `screenshots/` folder at the root of your project.

---

## 🎥 Screen Recording

https://github.com/yourusername/intellisoft-patient-app/assets/XXXXXXXXXXXX/screen_recording.mp4

> (You can upload the `.mp4` file to GitHub and paste the auto-generated link here.)

---

## ⚙️ Setup & Installation

### 1️⃣ Clone the repository
```bash```
git clone  https://github.com/Darkknight123/IntelliSoft.git
cd intellisoft

### 2️⃣ Open in Android Studio

Minimum Android Studio: Ladybug or newer

Minimum SDK: 24

### 3️⃣ Configure API Base URL

In ApiClient.kt, update your backend endpoint:
private const val BASE_URL = "https://your-api-server.com/api/"

### 4️⃣ Run the app 🚀

Select a device/emulator and click Run ▶️ in Android Studio.

## 🧩Project Structure

com.app.intellisoft/
├── data/
│   ├── local/           # Room & TokenManager
│   ├── remote/          # API services & models
│   └── repository/      # Data handling
├── presentation/
│   ├── screens/         # UI Composables
│   ├── viewmodel/       # ViewModels
│   └── navigation/      # Navigation graph
└── utils/               # Helpers & constants

## 🤝 Contribution Guide

 1.Fork the repository

 2.Create your feature branch

  git checkout -b feature/amazing-feature

 3.Commit your changes

 git commit -m "Add amazing feature"

 4.Push to your branch

 git push origin feature/amazing-feature

 5.Open a Pull Request

## 🧑‍💻Maintainer

Kerry Philip
📧 [philiokerry57@gmail.com]


