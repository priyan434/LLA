package com.example.languagelearningapp


import QuizScreen
import TranslateScreen
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.languagelearningapp.ui.theme.LanguagelearningappTheme
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*


class MainActivity : ComponentActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var auth: FirebaseAuth
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeFirebase()
        initializeTextToSpeech()
        createAdminUser("admin@example.com", "123456", auth, database)

        setContent {
            LanguagelearningappTheme {
                AppNavigator(
                    navController = rememberNavController(),
                    textToSpeech = textToSpeech,
                    auth = auth,
                    firestore = firestore,
                    database = database
                )
            }
        }
    }
    fun createAdminUser(email: String, password: String, auth: FirebaseAuth, database: DatabaseReference) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    userId?.let {
                        // Set user as admin in the database
                        database.child("users").child(it).setValue(mapOf("isAdmin" to true))
                            .addOnSuccessListener {
                                Log.d("CreateAdminUser", "Admin user created successfully: $it")
                            }
                            .addOnFailureListener { e ->
                                Log.e("CreateAdminUser", "Failed to set user as admin: ${e.message}")
                            }
                    }
                } else {
                    Log.e("CreateAdminUser", "User creation failed: ${task.exception?.message}")
                }
            }
    }

    private fun initializeFirebase() {
        firestore = FirebaseFirestore.getInstance()
        firebaseAnalytics = Firebase.analytics
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
//        addDummyVocabularyData()
//        addDummyPhrases()
    }
    private fun addDummyPhrases() {
        val dummyPhrases = listOf(
            "Bonjour" to "Hello",
            "Comment ça va ?" to "How are you?",
            "Merci beaucoup" to "Thank you very much",
            "S'il vous plaît" to "Please",
            "Oui" to "Yes",
            "Non" to "No",
            "Excusez-moi" to "Excuse me",
            "Je suis désolé(e)" to "I am sorry",
            "Ça va bien, merci" to "I'm fine, thank you",
            "Quel est votre nom ?" to "What is your name?",
            "Je m'appelle ..." to "My name is ...",
            "Parlez-vous anglais ?" to "Do you speak English?",
            "Je ne comprends pas" to "I do not understand",
            "Pouvez-vous répéter, s'il vous plaît ?" to "Can you repeat, please?",
            "Où sont les toilettes ?" to "Where are the restrooms?",
            "Combien ça coûte ?" to "How much does it cost?",
            "Je voudrais ..." to "I would like ...",
            "C'est délicieux !" to "It’s delicious!",
            "J'ai besoin d'aide" to "I need help",
            "À bientôt" to "See you soon",
            "Bonne journée" to "Have a good day",
            "Bonsoir" to "Good evening",
            "Bonne nuit" to "Good night",
            "Félicitations" to "Congratulations",
            "Bienvenue" to "Welcome",
            "Enchanté(e)" to "Nice to meet you",
            "Ça ne fait rien" to "It doesn’t matter",
            "Je suis fatigué(e)" to "I am tired",
            "Je suis perdu(e)" to "I am lost"
        )

        dummyPhrases.forEachIndexed { index, phrase ->
            val (frenchPhrase, englishTranslation) = phrase
            // Push phrase to Firebase under a unique key
            database.child("phrases").child("item$index").setValue(mapOf("phrase" to frenchPhrase, "translation" to englishTranslation))
                .addOnSuccessListener {
                    Log.d("AddDummyPhrases", "Successfully added: $frenchPhrase -> $englishTranslation")
                }
                .addOnFailureListener { e ->
                    Log.e("AddDummyPhrases", "Failed to add: $frenchPhrase -> $englishTranslation. Error: ${e.message}")
                }
        }
    }

    private fun addDummyVocabularyData() {
        val dummyVocabulary = listOf(
            "Pomme" to "Apple",
            "Orange" to "Orange",
            "Banane" to "Banana",
            "Raisin" to "Grape",
            "Pêche" to "Peach",
            "Ananas" to "Pineapple",
            "Fraise" to "Strawberry",
            "Mangue" to "Mango",
            "Pastèque" to "Watermelon",
            "Cerise" to "Cherry",
            "Citron" to "Lemon",
            "Kiwi" to "Kiwi",
            "Myrtille" to "Blueberry",
            "Papaye" to "Papaya",
            "Noix de coco" to "Coconut",
            "Poire" to "Pear",
            "Avocat" to "Avocado",
            "Figue" to "Fig",
            "Framboise" to "Raspberry",
            "Mûre" to "Blackberry",
            "Tomate" to "Tomato",
            "Carotte" to "Carrot",
            "Lait" to "Milk",
            "Pain" to "Bread",
            "Fromage" to "Cheese",
            "Viande" to "Meat",
            "Poisson" to "Fish",
            "Oeuf" to "Egg",
            "Riz" to "Rice",
            "Pâtes" to "Pasta"
        )

        dummyVocabulary.forEachIndexed { index, vocab ->
            val (word, translation) = vocab
            // Push vocabulary to Firebase under a unique key
            database.child("vocabulary").child("item$index").setValue(mapOf("word" to word, "translation" to translation))
                .addOnSuccessListener {
                    Log.d("AddDummyVocabulary", "Successfully added: $word -> $translation")
                }
                .addOnFailureListener { e ->
                    Log.e("AddDummyVocabulary", "Failed to add: $word -> $translation. Error: ${e.message}")
                }
        }
    }

    private fun initializeTextToSpeech() {
        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.language = Locale.FRENCH
            } else {
                Toast.makeText(this, "Text-to-Speech initialization failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        textToSpeech.stop()
        textToSpeech.shutdown()
        super.onDestroy()
    }
}

@Composable
fun AppNavigator(
    navController: NavHostController,
    textToSpeech: TextToSpeech,
    auth: FirebaseAuth,
    firestore: FirebaseFirestore,
    database: DatabaseReference
) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController = navController, auth = auth, database = database)
        }
        composable("signup") {
            SignupScreen(navController = navController, auth = auth)
        }
        composable("admin") {
            AdminScreen(database = database,navController=navController)
        }
        composable("add_phrase_screen") {
            AddPhraseScreen(database = database)
        }
        composable("add_vocabulary_screen") {
            AddVocabularyScreen(database = database, onNavigateBack = { navController.popBackStack() })
        }

        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("vocabulary") {
            VocabularyScreen(database=database,textToSpeech = textToSpeech)
        }
        composable("alphabets") {
            AlphabetScreen(textToSpeech = textToSpeech)
        }
        composable("phrases") {
            PhrasesScreen(textToSpeech = textToSpeech,database=database)
        }
        composable("translate") {
            TranslateScreen(textToSpeech = textToSpeech)
        }
        composable("quiz") {
            QuizScreen(quizQuestions, navController = navController)
        }
    }
}
