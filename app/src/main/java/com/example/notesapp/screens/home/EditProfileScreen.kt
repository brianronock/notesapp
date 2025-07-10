package com.example.notesapp.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.UserProfileChangeRequest
import com.example.notesapp.auth.GoogleAuthUiClient

@Composable
fun EditProfileScreen(
    navController: NavController,
    googleAuthUiClient: GoogleAuthUiClient
) {
    val user = googleAuthUiClient.getCurrentUser()
    var newName by remember { mutableStateOf(user?.displayName ?: "") }
    var message by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Edit Profile", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = newName,
            onValueChange = { newName = it },
            label = { Text("Display Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            user?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(newName)
                    .build()
            )?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    message = "Profile updated!"
                } else {
                    message = "Update failed: ${task.exception?.message}"
                }
            }
        }) {
            Text("Save Changes")
        }

        message?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(it)
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = { navController.navigateUp() }) {
            Text("Back to Profile")
        }
    }
}