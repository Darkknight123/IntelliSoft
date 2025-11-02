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

| Onboarding | Patient List | Patient Registration | Add Vitals | Assessment |
|-------------|--------------|-------------|-------------|
| ![Onboarding](<img width="446" height="937" alt="image" src="https://github.com/user-attachments/assets/2a4dbd9e-33bd-4590-8dd6-ac4625d672bf" />
) | ![Patient List](<img width="446" height="937" alt="image" src="https://github.com/user-attachments/assets/aa02877f-805d-4759-b078-7f9d576435db" /> | ![Patient Registration (<img width="446" height="937" alt="image" src="https://github.com/user-attachments/assets/6ecbd9a7-649b-4f95-bc7b-3e4f44223ae1" />
)]
) | ![Vitals](<img width="446" height="937" alt="image" src="https://github.com/user-attachments/assets/87be109a-fd2c-4454-9a41-ad03305749ff" />
) | ![Assessment](<img width="446" height="937" alt="image" src="https://github.com/user-attachments/assets/a02868a2-800d-448c-b387-ac3cff86bfbd" />
) |

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
📧 [philipkerry57@gmail.com]


