package com.example.languagelearningapp

import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import kotlin.math.ceil

@Composable
fun VocabularyScreen(database: DatabaseReference, textToSpeech: TextToSpeech) {
    var vocabularyList by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var currentPage by remember { mutableStateOf(1) }
    val itemsPerPage = 5

    // Fetch vocabulary data from Firebase
    LaunchedEffect(Unit) {
        database.child("vocabulary").get()
            .addOnSuccessListener { dataSnapshot ->
                val vocabList = mutableListOf<Pair<String, String>>()
                dataSnapshot.children.forEach { snapshot ->
                    val word = snapshot.child("word").getValue(String::class.java) ?: ""
                    val translation = snapshot.child("translation").getValue(String::class.java) ?: ""
                    vocabList.add(word to translation)
                }
                vocabularyList = vocabList
                loading = false
            }
            .addOnFailureListener {
                loading = false
            }
    }

    val totalPages = if (vocabularyList.isNotEmpty()) ceil(vocabularyList.size / itemsPerPage.toDouble()).toInt() else 1
    val startIndex = (currentPage - 1) * itemsPerPage
    val endIndex = (startIndex + itemsPerPage).coerceAtMost(vocabularyList.size)
    val currentVocabulary = vocabularyList.subList(startIndex, endIndex)

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
        } else if (currentVocabulary.isNotEmpty()) {
            VocabularyList(textToSpeech, currentVocabulary)

            Spacer(modifier = Modifier.height(16.dp))

            // Pagination controls
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { if (currentPage > 1) currentPage-- },
                    enabled = currentPage > 1
                ) {
                    Text("Previous")
                }

                Text("Page $currentPage of $totalPages")

                Button(
                    onClick = { if (currentPage < totalPages) currentPage++ },
                    enabled = currentPage < totalPages
                ) {
                    Text("Next")
                }
            }
        } else {
            Text("No vocabulary available.")
        }
    }
}

@Composable
fun VocabularyList(textToSpeech: TextToSpeech, vocabulary: List<Pair<String, String>>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(vocabulary) { vocabularyItem -> // vocabularyItem is a Pair<String, String>
            val (word, translation) = vocabularyItem // Destructure Pair
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
                // Speak the word using Text-to-Speech
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
                imageVector = Icons.Default.VolumeUp,
                contentDescription = "Play pronunciation",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

