package com.example.notesapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.remember // Import remember
import androidx.navigation.compose.rememberNavController
import com.example.notesapp.auth.GoogleAuthUiClient // Import your GoogleAuthUiClient
import com.example.notesapp.navigation.NavGraph // Corrected name from AppNavigation to NavGraph if that's what you use
import com.example.notesapp.ui.theme.NotesAppTheme
import com.example.notesapp.viewmodel.NotesViewModel
// FirebaseAuth instance might not be directly needed here anymore if GoogleAuthUiClient handles all auth state
// import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    // private val auth = FirebaseAuth.getInstance() // You might not need this instance directly here
    // if GoogleAuthUiClient and ViewModels handle auth state.
    private val notesViewModel: NotesViewModel by viewModels()

    // Declare googleAuthUiClient as a property of MainActivity
    // Initialize it lazily or in onCreate before setContent
    private lateinit var googleAuthUiClient: GoogleAuthUiClient

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize GoogleAuthUiClient here, as it needs a Context
        googleAuthUiClient = GoogleAuthUiClient(applicationContext) // Or use 'this' for Activity context

        setContent {
            NotesAppTheme {
                val navController = rememberNavController()

                // Pass the googleAuthUiClient instance to your NavGraph
                NavGraph(
                    navController = navController,
                    notesViewModel = notesViewModel,
                    googleAuthUiClient = googleAuthUiClient // Pass the created instance
                )
            }
        }
    }
}

