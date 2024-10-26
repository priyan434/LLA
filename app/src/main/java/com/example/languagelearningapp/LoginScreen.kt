package com.example.languagelearningapp

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

@Composable
fun LoginScreen(navController: NavController, auth: FirebaseAuth, database: DatabaseReference) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login to French Learning", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (loading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        loginUser(auth, email, password, navController, database, context) { isLoading ->
                            loading = isLoading
                        }
                    } else {
                        Toast.makeText(context, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading // Disable button while loading
            ) {
                Text("Login")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = {
                navController.navigate("signup")
            }
        ) {
            Text("Don't have an account? Sign Up")
        }
    }
}

// Separate function to handle user login logic
fun loginUser(
    auth: FirebaseAuth,
    email: String,
    password: String,
    navController: NavController,
    database: DatabaseReference,
    context: android.content.Context,
    setLoading: (Boolean) -> Unit
) {
    setLoading(true) // Start loading

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val currentUser = auth.currentUser
                currentUser?.let { user ->
                    Log.d("LoginScreen", "Login successful for user: ${user.uid}")

                    // Retrieve user data from the database
                    database.child("users").child(user.uid).get()
                        .addOnSuccessListener { snapshot ->
                            val isAdmin = snapshot.child("isAdmin").getValue(Boolean::class.java) ?: false
                            Log.d("LoginScreen", "User data retrieved: $snapshot")

                            setLoading(false) // Stop loading

                            // Navigate based on admin status
                            if (isAdmin) {
                                navController.navigate("admin")
                            } else {
                                navController.navigate("home")
                            }
                        }
                        .addOnFailureListener { exception ->
                            setLoading(false) // Stop loading
                            Log.e("LoginScreen", "Failed to retrieve user data: ${exception.message}")
                            Toast.makeText(context, "Failed to retrieve user data: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                setLoading(false) // Stop loading
                Log.e("LoginScreen", "Login failed: ${task.exception?.message}")
                Toast.makeText(context, "Login failed: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
}
