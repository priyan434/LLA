package com.example.languagelearningapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun AuthScreen(
    title: String,
    onSubmit: (String, String) -> Unit,
    onToggle: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginStatus by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title, style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Submit Button
        Button(
            onClick = {
                onSubmit(email, password)
                if (email == "test@example.com" && password == "password123") {
                    loginStatus = "Login successful!"
                } else {
                    loginStatus = "Login failed. Incorrect credentials."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = title)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Toggle between Login and Signup
        TextButton(onClick = onToggle) {
            Text(text = if (title == "Login") "Don't have an account? Sign up" else "Already have an account? Log in")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display Login Status
        loginStatus?.let {
            Text(text = it, color = if (it.contains("successful")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
        }
    }
}
