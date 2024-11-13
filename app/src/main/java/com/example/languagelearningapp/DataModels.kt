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
    ),
    QuizQuestion(
        question = "What is the French word for 'No'?",
        options = listOf("Non", "Oui", "Merci", "Bonjour"),
        correctAnswer = "Non"
    ),
    QuizQuestion(
        question = "What is the French phrase for 'How are you?'",
        options = listOf("Ça va bien", "S'il vous plaît", "Merci beaucoup", "Comment ça va ?"),
        correctAnswer = "Comment ça va ?"
    ),
    QuizQuestion(
        question = "What does 'S'il vous plaît' mean?",
        options = listOf("Please", "Thank you", "Excuse me", "Hello"),
        correctAnswer = "Please"
    ),
    QuizQuestion(
        question = "What is the French phrase for 'I am sorry'?",
        options = listOf("Je suis fatigué", "Je suis désolé(e)", "Excusez-moi", "Ça va bien"),
        correctAnswer = "Je suis désolé(e)"
    ),
    QuizQuestion(
        question = "What is the French phrase for 'Where are the restrooms?'",
        options = listOf("Où est la bibliothèque ?", "Où sont les toilettes ?", "Combien ça coûte ?", "Excusez-moi"),
        correctAnswer = "Où sont les toilettes ?"
    ),
    QuizQuestion(
        question = "What is the French word for 'Delicious'?",
        options = listOf("Délicieux", "Chaud", "Froid", "Grand"),
        correctAnswer = "Délicieux"
    ),
    QuizQuestion(
        question = "What does 'Je m'appelle ...' mean?",
        options = listOf("I am tired", "My name is ...", "I need help", "I am lost"),
        correctAnswer = "My name is ..."
    ),
    QuizQuestion(
        question = "What is the French phrase for 'Can you repeat, please?'",
        options = listOf("S'il vous plaît", "Excusez-moi", "Pouvez-vous répéter, s'il vous plaît ?", "Merci"),
        correctAnswer = "Pouvez-vous répéter, s'il vous plaît ?"
    ),
    QuizQuestion(
        question = "What is the French word for 'Good evening'?",
        options = listOf("Bonjour", "Bonsoir", "Bonne nuit", "Merci"),
        correctAnswer = "Bonsoir"
    ),
    QuizQuestion(
        question = "What does 'Félicitations' mean?",
        options = listOf("Congratulations", "Goodbye", "Excuse me", "I'm fine"),
        correctAnswer = "Congratulations"
    ),
    QuizQuestion(
        question = "What is the French phrase for 'I need help'?",
        options = listOf("Je suis désolé", "J'ai besoin d'aide", "Merci beaucoup", "Ça va bien"),
        correctAnswer = "J'ai besoin d'aide"
    ),
    QuizQuestion(
        question = "What does 'À bientôt' mean?",
        options = listOf("See you soon", "Good night", "Have a good day", "Excuse me"),
        correctAnswer = "See you soon"
    )
)
