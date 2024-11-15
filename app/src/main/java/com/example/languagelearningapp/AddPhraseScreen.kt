package com.example.languagelearningapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

@Composable
fun AddPhraseScreen(database: DatabaseReference) {
    var phrase by remember { mutableStateOf("") }
    var translation by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var phrases by remember { mutableStateOf(emptyList<Pair<String, Map<String, String>>>()) }

    // Load existing phrases from Firebase
    LaunchedEffect(Unit) {
        database.child("phrases").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val loadedPhrases = mutableListOf<Pair<String, Map<String, String>>>()
                for (child in snapshot.children) {
                    val phraseData = child.getValue<Map<String, String>>()
                    if (phraseData != null) {
                        loadedPhrases.add((child.key!! to phraseData) as Pair<String, Map<String, String>>)
                    }
                }
                phrases = loadedPhrases
            }

            override fun onCancelled(error: DatabaseError) {
                message = "Failed to load phrases: ${error.message}"
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Add a New Phrase", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = phrase,
            onValueChange = { phrase = it },
            label = { Text("Phrase") },
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
                if (phrase.isNotEmpty() && translation.isNotEmpty()) {
                    val phraseData = mapOf("phrase" to phrase, "translation" to translation)
                    database.child("phrases").push().setValue(phraseData)
                        .addOnSuccessListener {
                            message = "Phrase added successfully!"
                            phrase = ""
                            translation = ""
                        }
                        .addOnFailureListener {
                            message = "Failed to add phrase: ${it.message}"
                        }
                } else {
                    message = "Please fill both fields."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Phrase")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = message, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Existing Phrases", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(phrases) { (id, phraseData) ->
                PhraseItem(
                    id = id,
                    phrase = phraseData["phrase"] ?: "",
                    translation = phraseData["translation"] ?: "",
                    database = database
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun PhraseItem(id: String, phrase: String, translation: String, database: DatabaseReference) {
    var isEditing by remember { mutableStateOf(false) }
    var editPhrase by remember { mutableStateOf(phrase) }
    var editTranslation by remember { mutableStateOf(translation) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .then(if (isEditing) Modifier else Modifier)
    ) {
        if (isEditing) {
            OutlinedTextField(
                value = editPhrase,
                onValueChange = { editPhrase = it },
                label = { Text("Edit Phrase") },
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
                    val updatedData = mapOf("phrase" to editPhrase, "translation" to editTranslation)
                    database.child("phrases").child(id).setValue(updatedData)
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
            Text(text = "Phrase: $phrase", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Translation: $translation", style = MaterialTheme.typography.bodyMedium)

            Row(modifier = Modifier.padding(top = 8.dp)) {
                Button(onClick = { isEditing = true }) {
                    Text("Update")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    database.child("phrases").child(id).removeValue()
                }) {
                    Text("Delete")
                }
            }
        }
    }
}
