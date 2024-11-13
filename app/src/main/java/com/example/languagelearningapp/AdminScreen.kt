package com.example.languagelearningapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

@Composable
fun AdminScreen(database: DatabaseReference, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Admin - Main Screen", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Button to navigate to AddPhraseScreen
        Button(
            onClick = { navController.navigate("add_phrase_screen") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to Add Phrase Screen")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to navigate to AddVocabularyScreen
        Button(
            onClick = { navController.navigate("add_vocabulary_screen") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to Add Vocabulary Screen")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Logout Button
        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut() // Sign out user
                navController.navigate("login") {
                    popUpTo(0) // Clears the back stack
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }
    }
}
