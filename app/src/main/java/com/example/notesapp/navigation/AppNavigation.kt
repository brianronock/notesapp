package com.example.notesapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue // Import for delegated property
import androidx.compose.runtime.remember // Import remember
import androidx.compose.ui.platform.LocalContext // Import LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.notesapp.auth.GoogleAuthUiClient // Import your client
import com.example.notesapp.screens.auth.LoginScreen
import com.example.notesapp.screens.home.HomeScreen
import com.example.notesapp.screens.note.AddNoteScreen
import com.example.notesapp.screens.auth.ProfileScreen
import com.example.notesapp.screens.note.ViewNoteScreen
import com.example.notesapp.viewmodel.NotesViewModel
import com.google.firebase.auth.ktx.auth // For checking current user
import com.google.firebase.ktx.Firebase // For Firebase services

@RequiresApi(Build.VERSION_CODES.O) // Still here, assess if needed for other screens
@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()
    val notesViewModel: NotesViewModel = viewModel()

    // Get the current context
    val context = LocalContext.current

    // Create an instance of GoogleAuthUiClient
    // Use remember to keep the same instance across recompositions unless context changes
    val googleAuthUiClient by remember(context) {
        lazy { // Use lazy to initialize it only when first needed (optional but good practice)
            GoogleAuthUiClient(context)
        }
    }

    // Determine the start destination based on whether the user is already signed in
    val firebaseAuth = Firebase.auth
    val startDestination = if (firebaseAuth.currentUser != null) {
        "home" // If user is signed in, start at home
    } else {
        "login" // Otherwise, start at login
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            // Pass the googleAuthUiClient to your LoginScreen
            LoginScreen(
                navController = navController,
                googleAuthUiClient = googleAuthUiClient
            )
        }
        composable("home") {
            // You might want to pass googleAuthUiClient to HomeScreen as well
            // if it needs to handle sign-out or display user info.
            HomeScreen(
                navController = navController,
                notesViewModel = notesViewModel
                // googleAuthUiClient = googleAuthUiClient // Optional: if HomeScreen needs it
            )
        }
        composable("add_note") {
            AddNoteScreen(navController, notesViewModel)
        }
        composable("profile") {
            // ProfileScreen will likely need googleAuthUiClient for sign-out
            // and displaying user information.
            ProfileScreen(
                navController = navController,
                googleAuthUiClient = googleAuthUiClient // Pass it here
            )
        }
        composable("view_note/{noteIndex}") { backStackEntry ->
            val noteIndexString = backStackEntry.arguments?.getString("noteIndex")
            // It's safer to use note IDs from Firestore if you've migrated
            // For now, continuing with noteIndex if that's still your primary key locally
            noteIndexString?.toIntOrNull()?.let { noteIndex ->
                ViewNoteScreen(navController, notesViewModel, noteIndex)
            }
        }
    }
}

