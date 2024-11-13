package com.example.languagelearningapp

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(navController: NavController) {
    // Handle back press to exit app
    BackHandler {
        // Exit the application when back button is pressed on HomeScreen
        // This may not work as intended on all devices; some may need additional handling
        // To ensure a proper exit, you might want to handle this differently
        // For example, using System.exit(0) may be more appropriate in some contexts
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to French Learning", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("vocabulary") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Learn Vocabulary")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { navController.navigate("phrases") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Learn Phrases")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { navController.navigate("alphabets") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Learn Alphabets")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { navController.navigate("translate") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Translate Text")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { navController.navigate("quiz") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Take a Quiz")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Logout Button
        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut() // Sign out the user
                navController.navigate("login") {
                    popUpTo(0) // Clear the back stack to prevent going back to home screen
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }
    }
}
