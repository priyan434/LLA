package com.example.languagelearningapp

import androidx.compose.foundation.layout.* // For layout components like Column, Spacer, etc.
import androidx.compose.material3.* // For Material 3 components like Text, Button, etc.
import androidx.compose.runtime.* // For state management (remember, mutableStateOf)
import androidx.compose.ui.Modifier // For using the Modifier in UI elements
import androidx.compose.ui.platform.LocalContext // For accessing context
import androidx.compose.ui.text.input.PasswordVisualTransformation // For password masking in TextFields
import androidx.compose.ui.unit.dp // For setting padding and spacing dimensions
import androidx.navigation.NavController // For navigation between screens
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.Alignment // For alignment components
import android.widget.Toast // For showing toast messages

@Composable
fun SignupScreen(navController: NavController, auth: FirebaseAuth) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) } // Track loading state
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Sign Up for French Learning", style = MaterialTheme.typography.headlineMedium)

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
            CircularProgressIndicator() // Show loading indicator when in progress
        } else {
            Button(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        loading = true // Set loading to true
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                loading = false // Set loading to false after task is done
                                if (task.isSuccessful) {
                                    Toast.makeText(context, "Signup successful!", Toast.LENGTH_SHORT).show()
                                    // Navigate to home screen
                                    navController.navigate("home")
                                } else {
                                    errorMessage = task.exception?.message ?: "Signup failed"
                                }
                            }
                    } else {
                        errorMessage = "Email and Password cannot be empty"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Up")
            }
        }

        // Display error message if present
        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = {
                // Navigate to login screen
                navController.navigate("login")
            }
        ) {
            Text("Already have an account? Login")
        }
    }
}
