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
