

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

### 3. 🔑 Set Web Client ID in Code

Open `GoogleAuthUiClient.kt` and replace the `setServerClientId(...)` value with your **Web client ID** from the Firebase console:

```kotlin
.setServerClientId("YOUR_WEB_CLIENT_ID_HERE")
```

You can find this value under **Firebase Console > Project Settings > OAuth 2.0 Client IDs**.

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

---

## 🧪 Firebase SHA-1 Setup (Required for Google Sign-In)

1. Generate your app’s SHA-1 fingerprint using this command:
   ```bash
   ./gradlew signingReport
   ```
   Look under `Variant: debug` for the `SHA1` key.

2. Go to [Firebase Console > Project Settings > Your apps > Add Fingerprint](https://console.firebase.google.com/)

3. Paste your SHA-1 and save.

4. Re-download the updated `google-services.json` and replace the one in:
   ```
   NotesApp/app/google-services.json
   ```

5. Sync Gradle again.

---

## 📁 Project Structure (Complete with Descriptions)

```
NotesApp/
├── app/
│   ├── auth/                     
│   │   └── GoogleAuthUiClient.kt       # Handles Google One Tap authentication logic and Firebase integration
│
│   ├── data/
│   │   └── NotesRepository.kt          # Provides abstraction over Firestore data operations for notes
│
│   ├── model/
│   │   ├── Note.kt                     # Data class representing a note (title, content, tags, timestamp)
│   │   └── User.kt                     # Data class representing user details (name, email, UID)
│
│   ├── ui/
│   │   ├── screens/
│   │   │   ├── HomeScreen.kt           # Displays welcome message, list of recent notes, and quick actions
│   │   │   ├── LoginScreen.kt          # UI for login with email/password or Google Sign-In
│   │   │   ├── ProfileScreen.kt        # Shows user profile info and basic note statistics
│   │   │   ├── RegisterScreen.kt       # UI for registering a new account with email and password
│   │   │   └── AddEditNoteScreen.kt    # Screen to create or edit a note, including tag management
│   │   └── components/                 
│   │       ├── TopBar.kt               # Custom top bar used across screens
│   │       ├── NoteCard.kt             # Reusable composable for displaying a single note
│   │       └── ...                     # Other UI components (e.g., buttons, fields, etc.)
│
│   ├── viewmodel/
│   │   ├── AuthViewModel.kt            # Manages login, registration, and Google Sign-In state
│   │   ├── NotesViewModel.kt           # Holds list of notes, handles creation, update, and deletion
│   │   └── ProfileViewModel.kt         # Manages user data, statistics, and logout logic
│
│   ├── navigation/
│   │   └── NavGraph.kt                 # Defines composable navigation routes using Jetpack Navigation
│
│   └── MainActivity.kt                 # App entry point; sets up navigation and theme
```

---

## ⚠️ Incomplete Features & Areas to Improve

- 👤 Add Google profile picture and display name to Profile screen
- 🛠 Implement working Edit Profile button
- 📈 Finish statistics calculations:
  - Most Active Day
  - Favorite Category based on tags
- 🏷 Organize notes by tag and make them searchable
- 💬 Add note sharing functionality
- 🧽 UI polish and animations for smoother transitions
- 🌐 Google Sign-In button branding (optional)
```