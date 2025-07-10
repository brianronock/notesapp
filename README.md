

# 📒 NotesApp (Jetpack Compose + Firebase)

NotesApp is a modern note-taking Android app built using **Jetpack Compose**, **Firebase Authentication**, and **Cloud Firestore**. It supports:
- Email/password login
- Google Sign-In (One Tap)
- Viewing and editing notes
- Profile and note-taking statistics

---

## ✅ Features

- 🔐 Email & Password Authentication
- 🔑 Google One Tap Sign-In
- 🏠 Home screen with welcome message & recent notes
- 📝 Create, tag, and save notes
- 📊 Profile view with note statistics
- 🧭 Bottom navigation for quick access
- 💾 Firebase Firestore backend

---

## 🚀 Getting Started

### 1. 🔧 Clone the Project

```bash
git clone https://github.com/YOUR_USERNAME/NotesApp.git
cd NotesApp
```

---

### 2. 📲 Set Up Firebase

#### 🔥 Create Firebase Project

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click **Add Project** → name it `NotesApp` → continue with default settings.

#### 🔗 Add Android App to Firebase

1. Register Android app using your package name (e.g. `com.example.notesapp`)
2. Download the `google-services.json` file.
3. Place the file in:  
   `NotesApp/app/google-services.json`

#### 🔐 Enable Firebase Authentication

- Go to **Authentication > Sign-in method**
- Enable the following providers:
  - ✅ Email/Password
  - ✅ Google

##### ⚠️ Important for Google Sign-In
- Still in Firebase console:
  - Go to **Project Settings > General**
  - Under **Your apps**, find **Web API Key** and **Web client ID**
  - Copy the **Web client ID** from the "OAuth 2.0 Client IDs" section

---

### 3. 🔑 Configure `local.properties`

> ⚠️ Do NOT share this file or commit it to Git. It's in `.gitignore` by default.

Inside `local.properties`, add:

```
sdk.dir=/Users/YOUR_USERNAME/Library/Android/sdk
WEB_CLIENT_ID=YOUR_WEB_CLIENT_ID_FROM_FIREBASE
```

If this line is missing, the app will use a dummy client ID that will not work for Google login:
```kotlin
BuildConfig.WEB_CLIENT_ID = "MISSING_WEB_CLIENT_ID"
```

---

### 4. 📦 Sync Gradle and Build

In Android Studio:

1. **File > Sync Gradle**
2. Or from terminal:
```bash
./gradlew clean build
```

---

### 5. ▶️ Run the App

- Open `MainActivity.kt`
- Run the app on an emulator or connected device

---

## 🔐 Login Options

### Email/Password Login
- Register with email & password
- Login using the same credentials

### Google Sign-In
- Tap “Sign in with Google”
- Uses One Tap authentication
- Requires valid `WEB_CLIENT_ID`

---

## 🛠 Tech Stack

- Kotlin
- Jetpack Compose
- Firebase Authentication
- Cloud Firestore
- MVVM Architecture
- Material 3 UI
- Gradle Version Catalogs

---

## 📁 Project Structure (Summary)

```
NotesApp/
├── app/
│   ├── auth/               # GoogleAuthUiClient + Auth logic
│   ├── data/               # Firestore repository
│   ├── model/              # Note, User model classes
│   ├── ui/                 # Compose screens
│   └── viewmodel/          # ViewModels for Login, Notes, Profile
```
```