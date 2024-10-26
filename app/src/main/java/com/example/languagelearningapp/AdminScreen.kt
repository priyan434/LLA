package com.example.languagelearningapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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

        // Navigate to AddPhraseScreen
        Button(
            onClick = { navController.navigate("add_phrase_screen") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to Add Phrase Screen")
        }
    }
}
