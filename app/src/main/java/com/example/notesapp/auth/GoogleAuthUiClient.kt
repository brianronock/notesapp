package com.example.notesapp.auth

import android.content.Context
import android.content.Intent // Required for the new method
import android.content.IntentSender
import android.util.Log // For better logging
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException


// Context can remain private with this approach
class GoogleAuthUiClient(private val context: Context) {

    private val oneTapClient: SignInClient = Identity.getSignInClient(context)
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId("639251954503-e7ta4hf8lfl8dc4kj06ndl2ausf5l4dt.apps.googleusercontent.com").setFilterByAuthorizedAccounts(false) // Set to true if you only want to show accounts already used with your app
                .build()
        )
        .setAutoSelectEnabled(true) // Optional: enables automatic sign-in if possible
        .build()

    suspend fun getSignInIntentSender(): IntentSender? {
        return try {
            val result = oneTapClient.beginSignIn(signInRequest).await()
            result.pendingIntent.intentSender
        } catch (e: Exception) {
            if (e is CancellationException) throw e // Don't suppress coroutine cancellations
            Log.e("GoogleAuthUiClient", "getSignInIntentSender failed", e)
            null
        }
    }

    /**
     * Extracts the Google ID Token from the sign-in intent result.
     * @param intent The intent received from the Google Sign-In activity result.
     * @return The Google ID Token as a String, or null if extraction fails.
     */
    fun getGoogleIdTokenFromIntent(intent: Intent?): String? {
        return try {
            val credential = oneTapClient.getSignInCredentialFromIntent(intent)
            credential.googleIdToken
        } catch (e: Exception) {
            Log.e("GoogleAuthUiClient", "getGoogleIdTokenFromIntent failed", e)
            null
        }
    }

    fun signInWithGoogle(idToken: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception ?: Exception("Unknown error during Firebase sign-in with Google credential."))
                }
            }
    }

    fun signOut(onComplete: () -> Unit = {}) { // Added optional callback
        auth.signOut()
        // Also sign out from Google One Tap to ensure the user is fully signed out
        // and will be prompted again next time.
        CoroutineScope(Dispatchers.IO).launch { // Use a proper scope if available
            try {
                oneTapClient.signOut().await()
                Log.d("GoogleAuthUiClient", "Google One Tap sign out successful.")
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Log.e("GoogleAuthUiClient", "Google One Tap sign out failed.", e)
            } finally {
                onComplete() // Call completion callback regardless of one-tap sign-out outcome
            }
        }
    }

    // Helper to get current Firebase user
    fun getCurrentUser(): com.google.firebase.auth.FirebaseUser? {
        return auth.currentUser
    }
}

