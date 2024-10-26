package com.example.languagelearningapp

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.DatabaseReference
import kotlin.math.ceil

@Composable
fun PhrasesScreen(textToSpeech: TextToSpeech, database: DatabaseReference) {
    var phrases by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var currentPage by remember { mutableStateOf(1) }
    val itemsPerPage = 5

    // Fetch phrases from Realtime Database
    LaunchedEffect(Unit) {
        database.child("phrases").get().addOnSuccessListener { dataSnapshot ->
            val phraseList = mutableListOf<Pair<String, String>>()
            dataSnapshot.children.forEach { snapshot ->
                val phrase = snapshot.child("phrase").getValue(String::class.java) ?: ""
                val translation = snapshot.child("translation").getValue(String::class.java) ?: ""
                phraseList.add(phrase to translation)
            }
            phrases = phraseList
            loading = false
        }.addOnFailureListener {
            loading = false
        }
    }

    val totalPages = if (phrases.isNotEmpty()) ceil(phrases.size / itemsPerPage.toDouble()).toInt() else 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "French Phrases",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (loading) {
            CircularProgressIndicator()
        } else if (phrases.isNotEmpty()) {
            val startIndex = (currentPage - 1) * itemsPerPage
            val endIndex = (startIndex + itemsPerPage).coerceAtMost(phrases.size)
            val currentPhrases = phrases.subList(startIndex, endIndex)

            PhrasesList(textToSpeech, currentPhrases)

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
            Text("No phrases available.")
        }
    }
}

@Composable
fun PhrasesList(textToSpeech: TextToSpeech, phrases: List<Pair<String, String>>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(phrases) { (phrase, translation) ->
            PhraseCardItem(phrase = phrase, translation = translation, textToSpeech = textToSpeech)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhraseCardItem(phrase: String, translation: String, textToSpeech: TextToSpeech) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, Color.Gray),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = phrase,
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = translation,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(
                onClick = {
                    // Speak the phrase using Text-to-Speech
                    textToSpeech.speak(phrase, TextToSpeech.QUEUE_FLUSH, null, null)
                },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Speak Phrase",
                    tint = Color.Gray
                )
            }
        }
    }
}
