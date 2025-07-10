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

#### 🔥 Enable Cloud Firestore

1. In the [Firebase Console](https://console.firebase.google.com/):
   - Select your project.
   - Go to **Build > Firestore Database**.
2. Click **Create Database**.
3. Choose **Start in test mode** (⚠️ for development only).
4. Select your preferred location and click **Enable**.

✅ This creates a Firestore instance for storing and syncing your app’s notes.

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
│   │   └── GoogleAuthUiClient.kt       # Handles Google One Tap authentication logic and integrates with FirebaseAuth
│
│   ├── components/
│   │   ├── BottomBar.kt                # Bottom navigation bar used across screens
│   │   └── FormattingButton.kt         # Rich text formatting button for styling note content
│
│   ├── data/
│   │   └── NotesRepository.kt          # Provides abstraction for interacting with Firestore (CRUD operations for notes)
│
│   ├── model/
│   │   └── Note.kt                     # Data class representing a note (title, content, tags, timestamp)
│   │   └── (TODO) User.kt              # (Suggestion) Add model for user data like display name, email, profile image
│
│   ├── navigation/
│   │   ├── AppNavigation.kt            # Contains top-level navigation setup and route configuration
│   │   └── NavGraph.kt                 # Defines composable destinations and screen transitions using Jetpack Navigation
│
│   ├── screens/
│   │   ├── auth/
│   │   │   ├── LoginScreen.kt          # UI for signing in using email/password or Google Sign-In
│   │   │   ├── ProfileScreen.kt        # Displays logged-in user's info, logout button, and placeholder for stats
│   │   │   └── RegisterScreen.kt       # UI for creating a new user account with email and password
│   │   ├── home/
│   │   │   ├── EditProfileScreen.kt    # Screen for editing user display name (incomplete)
│   │   │   └── HomeScreen.kt           # Main screen showing welcome message, recent notes, and navigation
│   │   └── note/
│   │       ├── AddNoteScreen.kt        # Screen to create or edit notes, includes tagging and formatting UI
│   │       └── ViewNoteScreen.kt       # Displays the content of a selected note in read-only format
│
│   ├── ui/
│   │   └── theme/
│   │       ├── Color.kt                # Defines app color scheme and Material3 theming
│   │       ├── Theme.kt                # Root Compose Material theme setup
│   │       └── Type.kt                 # Font and typography definitions
│
│   ├── viewmodel/
│   │   ├── AuthViewModel.kt            # Handles user authentication state and events (login, register, logout)
│   │   └── NotesViewModel.kt           # Manages Firestore note data, live state for note list, create/edit/delete logic
│
│   ├── MainActivity.kt                 # App's main entry point; hosts the Compose UI and calls AppNavigation
│   └── NotesApp.kt                     # Composable app container applying theme and surface scaffold
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