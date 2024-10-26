package com.example.languagelearningapp

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.database.DatabaseReference

@Composable
fun VocabularyScreen(database: DatabaseReference, textToSpeech: TextToSpeech) {
    var vocabularyList by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var page by remember { mutableStateOf(0) }
    val pageSize = 5

    // Fetch vocabulary from Realtime Database
    LaunchedEffect(Unit) {
        database.child("vocabulary").get()
            .addOnSuccessListener { dataSnapshot ->
                val vocabulary = dataSnapshot.children.map { snapshot ->
                    val word = snapshot.child("word").getValue(String::class.java) ?: ""
                    val translation = snapshot.child("translation").getValue(String::class.java) ?: ""
                    word to translation
                }
                vocabularyList = vocabulary
                loading = false
            }
            .addOnFailureListener {
                loading = false
            }
    }

    // Pagination logic
    val paginatedList = vocabularyList.chunked(pageSize)
    val currentPage = paginatedList.getOrNull(page) ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Vocabulary List",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (loading) {
            CircularProgressIndicator()
        } else if (currentPage.isNotEmpty()) {
            VocabularyList(textToSpeech, currentPage)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (page > 0) {
                    Button(onClick = { page-- }) {
                        Text("Previous")
                    }
                }
                if (page < paginatedList.size - 1) {
                    Button(onClick = { page++ }) {
                        Text("Next")
                    }
                }
            }
        } else {
            Text("No vocabulary available.")
        }
    }
}

@Composable
fun VocabularyList(textToSpeech: TextToSpeech, vocabulary: List<Pair<String, String>>) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        vocabulary.forEach { (word, translation) ->
            VocabularyCardItem(word = word, translation = translation, textToSpeech = textToSpeech)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabularyCardItem(word: String, translation: String, textToSpeech: TextToSpeech) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // Speak the French word using Text-to-Speech
                textToSpeech.speak(word, TextToSpeech.QUEUE_FLUSH, null, null)
            },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = word,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = translation,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.VolumeUp, // Use AutoMirrored here
                contentDescription = "Play pronunciation",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewVocabularyScreen() {
    // Preview doesn't need real TextToSpeech instance, use a mock if needed
    // VocabularyScreen(TextToSpeech(context))
}
