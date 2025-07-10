package com.example.notesapp.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notesapp.auth.GoogleAuthUiClient // Import your client
import com.google.firebase.auth.FirebaseUser // For type safety

@Composable
fun ProfileScreen(
    navController: NavController,
    googleAuthUiClient: GoogleAuthUiClient
) {
    val currentUser = googleAuthUiClient.getCurrentUser()
    var isSignedOut by remember { mutableStateOf(false) }

    if (isSignedOut) {
        LaunchedEffect(Unit) {
            navController.navigate("login") {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        currentUser?.let { user ->
            Text("Welcome,", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(user.displayName ?: "No name set", style = MaterialTheme.typography.titleLarge)
            Text(user.email ?: "No email", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text("UID: ${user.uid}", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(24.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                navController.navigate("edit_profile")
            }) {
                Text("Edit Profile")
            }
            Button(onClick = {
                googleAuthUiClient.signOut {
                    isSignedOut = true
                }
            }) {
                Text("Logout")
            }
        } ?: run {
            Text("You’re not signed in.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                navController.navigate("login") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    launchSingleTop = true
                }
            }) {
                Text("Back to Login")
            }
        }
    }
}