package com.example.notesapp.navigation // Or your actual package

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel // If you're getting viewModel here, but MainActivity already does
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
// Removed: import androidx.navigation.compose.rememberNavController // navController is passed in
import com.example.notesapp.auth.GoogleAuthUiClient // Import your client
import com.example.notesapp.screens.auth.LoginScreen
import com.example.notesapp.screens.auth.ProfileScreen
import com.example.notesapp.screens.auth.RegisterScreen
import com.example.notesapp.screens.home.EditProfileScreen
import com.example.notesapp.screens.home.HomeScreen
import com.example.notesapp.screens.note.AddNoteScreen
import com.example.notesapp.screens.note.ViewNoteScreen
import com.example.notesapp.viewmodel.NotesViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@RequiresApi(Build.VERSION_CODES.O) // Assess if needed for all screens
@Composable
// Function name should match what MainActivity calls (NavGraph)
// It now accepts googleAuthUiClient
fun NavGraph(
    navController: NavHostController,      // Passed from MainActivity
    notesViewModel: NotesViewModel,        // Passed from MainActivity
    googleAuthUiClient: GoogleAuthUiClient // Newly added parameter
) {
    // Determine the start destination based on whether the user is already signed in
    val firebaseAuth = Firebase.auth
    val startDestination = if (firebaseAuth.currentUser != null) {
        "home"
    } else {
        "login"
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(
                navController = navController,
                googleAuthUiClient = googleAuthUiClient // Pass it here
            )
        }
        composable("home") {
            HomeScreen(
                navController = navController,
                notesViewModel = notesViewModel
            )
        }
        composable("add_note") {
            AddNoteScreen(navController, notesViewModel)
        }
        composable("profile") {
            ProfileScreen(
                navController = navController,
                googleAuthUiClient = googleAuthUiClient // Pass it here
            )
        }
        composable("view_note/{noteIndex}") { backStackEntry ->
            val noteIndexString = backStackEntry.arguments?.getString("noteIndex")
            noteIndexString?.toIntOrNull()?.let { noteIndex ->
                ViewNoteScreen(navController, notesViewModel, noteIndex)
            }
        }
        composable("register") {
            RegisterScreen(navController)
        }
        composable("edit_profile") {
            EditProfileScreen(
                navController = navController,
                googleAuthUiClient = googleAuthUiClient
            )
        }
    }
}

