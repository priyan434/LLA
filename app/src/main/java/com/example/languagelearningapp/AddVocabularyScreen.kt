package com.example.languagelearningapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

@Composable
fun AddVocabularyScreen(database: DatabaseReference, onNavigateBack: () -> Unit) {
    var word by remember { mutableStateOf("") }
    var translation by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var vocabularyList by remember { mutableStateOf(emptyList<Pair<String, Map<String, String>>>()) }

    // Load existing vocabulary from Firebase
    LaunchedEffect(Unit) {
        database.child("vocabulary").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val loadedVocabulary = mutableListOf<Pair<String, Map<String, String>>>()
                for (child in snapshot.children) {
                    val vocabData = child.getValue<Map<String, String>>()
                    if (vocabData != null) {
                        loadedVocabulary.add((child.key!! to vocabData) as Pair<String, Map<String, String>>)
                    }
                }
                vocabularyList = loadedVocabulary
            }

            override fun onCancelled(error: DatabaseError) {
                message = "Failed to load vocabulary: ${error.message}"
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Add Vocabulary", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = word,
            onValueChange = { word = it },
            label = { Text("Word") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = translation,
            onValueChange = { translation = it },
            label = { Text("Translation") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (word.isNotEmpty() && translation.isNotEmpty()) {
                    val vocabularyData = mapOf("word" to word, "translation" to translation)
                    database.child("vocabulary").push().setValue(vocabularyData)
                        .addOnSuccessListener {
                            message = "Vocabulary added successfully!"
                            word = ""
                            translation = ""
                        }
                        .addOnFailureListener {
                            message = "Failed to add vocabulary: ${it.message}"
                        }
                } else {
                    message = "Please fill both fields."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Vocabulary")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = message, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Existing Vocabulary", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(vocabularyList) { (id, vocabData) ->
                VocabularyItem(
                    id = id,
                    word = vocabData["word"] ?: "",
                    translation = vocabData["translation"] ?: "",
                    database = database
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onNavigateBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Admin")
        }
    }
}

@Composable
fun VocabularyItem(id: String, word: String, translation: String, database: DatabaseReference) {
    var isEditing by remember { mutableStateOf(false) }
    var editWord by remember { mutableStateOf(word) }
    var editTranslation by remember { mutableStateOf(translation) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        if (isEditing) {
            OutlinedTextField(
                value = editWord,
                onValueChange = { editWord = it },
                label = { Text("Edit Word") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = editTranslation,
                onValueChange = { editTranslation = it },
                label = { Text("Edit Translation") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Button(onClick = {
                    val updatedData = mapOf("word" to editWord, "translation" to editTranslation)
                    database.child("vocabulary").child(id).setValue(updatedData)
                    isEditing = false
                }) {
                    Text("Save")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { isEditing = false }) {
                    Text("Cancel")
                }
            }
        } else {
            Text(text = "Word: $word", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Translation: $translation", style = MaterialTheme.typography.bodyMedium)

            Row(modifier = Modifier.padding(top = 8.dp)) {
                Button(onClick = { isEditing = true }) {
                    Text("Update")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    database.child("vocabulary").child(id).removeValue()
                }) {
                    Text("Delete")
                }
            }
        }
    }
}
