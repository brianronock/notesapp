package com.example.notesapp.screens.auth

import android.app.Activity
// import android.content.IntentSender // Not directly used in LoginScreen anymore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext // For creating GoogleAuthUiClient instance
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notesapp.auth.GoogleAuthUiClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController, googleAuthUiClient: GoogleAuthUiClient) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) } // For progress indication
    val scope = rememberCoroutineScope()

    val auth = FirebaseAuth.getInstance()

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        isLoading = true // Start loading
        if (result.resultCode == Activity.RESULT_OK) {
            // Use the new method from your client to get the ID token
            val googleIdToken = googleAuthUiClient.getGoogleIdTokenFromIntent(result.data)

            if (googleIdToken != null) {
                googleAuthUiClient.signInWithGoogle(googleIdToken, {
                    // Success
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                    isLoading = false
                }, { e ->
                    // Failure
                    error = e.message ?: "Google Sign-In failed during Firebase authentication."
                    Log.e("LoginScreenGoogle", "Firebase signInWithCredential failed", e)
                    isLoading = false
                })
            } else {
                error = "Could not retrieve Google ID Token from sign-in result."
                Log.e("LoginScreenGoogle", "Google ID Token is null from client processing.")
                isLoading = false
            }
        } else {
            error = "Google Sign-In was cancelled or failed. (Result code: ${result.resultCode})"
            Log.w("LoginScreenGoogle", "Google Sign-In failed or was cancelled by user. Result code: ${result.resultCode}")
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
            // visualTransformation = PasswordVisualTransformation() // For password visibility
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    error = "Email and password cannot be empty."
                    return@Button
                }
                isLoading = true
                error = null // Clear previous errors
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        isLoading = false
                        if (task.isSuccessful) {
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            error = task.exception?.message ?: "Email/Password sign-in failed."
                            Log.e("LoginScreenEmail", "signInWithEmailAndPassword failed", task.exception)
                        }
                    }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text("Sign in with Email")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = {
                scope.launch {
                    isLoading = true
                    error = null // Clear previous errors
                    try {
                        val intentSender = googleAuthUiClient.getSignInIntentSender()
                        if (intentSender != null) {
                            googleSignInLauncher.launch(
                                IntentSenderRequest.Builder(intentSender).build()
                            )
                            // isLoading will be set to false in the launcher's result callback
                        } else {
                            error = "Could not get Google Sign-In intent. Please try again."
                            Log.e("LoginScreenGoogle", "IntentSender is null from getSignInIntentSender")
                            isLoading = false
                        }
                    } catch (e: Exception) {
                        error = e.message ?: "Google Sign-in failed to start. Please try again."
                        Log.e("LoginScreenGoogle", "Error launching Google Sign-In", e)
                        isLoading = false
                    }
                }
            },
            enabled = !isLoading
        ) {
            Text("Sign in with Google")
        }

        error?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium // Made it slightly larger
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        TextButton(onClick = {
            if (!isLoading) {
                navController.navigate("register")
                error = "Signup screen not implemented yet."
            }
        }) {
            Text("Don't have an account? Sign Up")
        }
    }
}