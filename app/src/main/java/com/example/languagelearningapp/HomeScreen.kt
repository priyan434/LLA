package com.example.languagelearningapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
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

        // Navigate to the Phrases screen
        Button(
            onClick = { navController.navigate("phrases") }, // Added navigation to "phrases"
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

        // Navigate to the Translate Text screen
        Button(
            onClick = { navController.navigate("translate") }, // Added navigation to translate screen
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Translate Text")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { navController.navigate("quiz") }, // Navigate to Quiz Screen
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Take a Quiz")
        }
    }
}
