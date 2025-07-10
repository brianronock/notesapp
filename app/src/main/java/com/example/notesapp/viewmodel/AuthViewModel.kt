package com.example.notesapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.notesapp.models.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val isLoggedIn: Boolean
        get() = auth.currentUser != null

    fun register(
        name: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val avatarUrl = "https://source.boringavatars.com/beam/120/${userId}"
                    val user = User(
                        uid = userId,
                        name = name,
                        email = email,
                        profileImageUrl = avatarUrl,
                        registeredAt = Timestamp.now()
                    )
                    com.google.firebase.firestore.FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(userId)
                        .set(user)
                        .addOnSuccessListener {
                            Log.d("Register", "User saved in Firestore")
                            onSuccess()
                        }
                        .addOnFailureListener {
                            Log.e("Register", "Failed to save user: ${it.message}")
                            onError("Failed to save user profile.")
                        }
                } else {
                    onError(task.exception?.message ?: "Registration failed")
                }
            }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(task.exception?.message ?: "Login failed")
                }
            }
    }

    fun logout() {
        auth.signOut()
    }
}