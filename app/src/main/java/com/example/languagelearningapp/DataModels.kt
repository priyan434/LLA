package com.example.languagelearningapp

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswer: String
)

val quizQuestions = listOf(
    QuizQuestion(
        question = "What is the French word for 'Hello'?",
        options = listOf("Bonjour", "Merci", "Oui", "Non"),
        correctAnswer = "Bonjour"
    ),
    QuizQuestion(
        question = "What is the French word for 'Thank you'?",
        options = listOf("Oui", "Merci", "S'il vous plaît", "Non"),
        correctAnswer = "Merci"
    ),
    QuizQuestion(
        question = "What is the French word for 'Yes'?",
        options = listOf("Oui", "Non", "Merci", "S'il vous plaît"),
        correctAnswer = "Oui"
    )
)
