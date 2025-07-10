package com.example.notesapp.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
/**
 * A screen where users can register a new account using email and password.
 *
 * @param navController Navigation controller to switch between screens.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var name by remember { mutableStateOf("") }

    val auth = FirebaseAuth.getInstance()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Register") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth()
            )

            error?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = {
                    if (password != confirmPassword) {
                        error = "Passwords do not match."
                        return@Button
                    }

                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val firebaseUser = FirebaseAuth.getInstance().currentUser
                                val profileUpdates = UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build()

                                firebaseUser?.updateProfile(profileUpdates)
                                    ?.addOnCompleteListener {
                                        val userId = firebaseUser.uid
                                        val user = com.example.notesapp.models.User(
                                            uid = userId,
                                            name = name,
                                            email = email,
                                            profileImageUrl = "https://source.boringavatars.com/beam/120/${userId}",
                                            registeredAt = com.google.firebase.Timestamp.now()
                                        )

                                        Firebase.firestore.collection("users").document(userId).set(user)
                                            .addOnSuccessListener {
                                                navController.navigate("home")
                                            }
                                            .addOnFailureListener { e ->
                                                error = "Failed to save user data: ${e.message}"
                                            }
                                    }
                            } else {
                                error = task.exception?.message ?: "Registration failed."
                            }
                        }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Register")
            }

            TextButton(onClick = { navController.navigate("login") }) {
                Text("Already have an account? Login")
            }
        }
    }
}