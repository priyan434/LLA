package com.example.languagelearningapp

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.ui.Modifier

@Composable
fun AlphabetScreen(textToSpeech: TextToSpeech) {
    val frenchAlphabets = listOf(
        Pair("A", "Apple"),
        Pair("B", "Banana"),
        Pair("C", "Cherry"),
        Pair("D", "Date"),
        Pair("E", "Elderberry"),
        Pair("F", "Fig"),
        Pair("G", "Grape"),
        Pair("H", "Honeydew"),
        Pair("I", "Iceberg"),
        Pair("J", "Jackfruit"),
        Pair("K", "Kiwi"),
        Pair("L", "Lemon"),
        Pair("M", "Mango"),
        Pair("N", "Nectarine"),
        Pair("O", "Orange"),
        Pair("P", "Papaya"),
        Pair("Q", "Quince"),
        Pair("R", "Raspberry"),
        Pair("S", "Strawberry"),
        Pair("T", "Tangerine"),
        Pair("U", "Uva"),
        Pair("V", "Vanilla"),
        Pair("W", "Watermelon"),
        Pair("X", "Xigua"),
        Pair("Y", "Yuzu"),
        Pair("Z", "Zucchini")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "French Alphabets",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display the alphabet list in a grid format
        AlphabetGrid(frenchAlphabets, textToSpeech)
    }
}

@Composable
fun AlphabetGrid(alphabets: List<Pair<String, String>>, textToSpeech: TextToSpeech) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4), // Change this number to fit your design
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(alphabets) { (letter, word) ->
            AlphabetItem(letter, word, textToSpeech)
        }
    }
}

@Composable
fun AlphabetItem(letter: String, word: String, textToSpeech: TextToSpeech) {
    val color = Color((0xFF000000..0xFFFFFFFF).random()) // Random color for each item

    Box(
        modifier = Modifier
            .clickable {
                // Speak the alphabet letter when clicked
                textToSpeech.speak(letter, TextToSpeech.QUEUE_FLUSH, null, null)
            }
            .fillMaxWidth()
            .aspectRatio(1f) // Maintain a square aspect ratio
            .padding(16.dp)
            .background(color, shape = MaterialTheme.shapes.medium) // Background with rounded corners
            .padding(8.dp), // Padding inside the box
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = letter,
                fontSize = 32.sp, // Large font size for the alphabets
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = word,
                fontSize = 16.sp, // Smaller font size for the English word
                style = MaterialTheme.typography.bodyMedium
            )
            Icon(
                imageVector = Icons.Filled.VolumeUp,
                contentDescription = "Pronounce $letter",
                modifier = Modifier
                    .size(24.dp)
                    .padding(top = 4.dp)
                    .clickable {
                        textToSpeech.speak(letter, TextToSpeech.QUEUE_FLUSH, null, null)
                    },
                tint = Color.White
            )
        }
    }
}
