import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun TranslateScreen(textToSpeech: TextToSpeech) {
    var inputText by remember { mutableStateOf(TextFieldValue("")) }
    var translatedText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Translate Text", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Input text field for entering text to translate
        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Enter text in French") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to perform translation (mock translation for now)
        Button(
            onClick = {
                // Mock translation: Reverse the input as an example
                translatedText = inputText.text.reversed() // You can replace this with real translation logic
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Translate")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display the translated text
        Text(
            text = "Translation: $translatedText",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to play the pronunciation of the input text using Text-to-Speech
        Button(
            onClick = {
                textToSpeech.speak(inputText.text, TextToSpeech.QUEUE_FLUSH, null, null)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Play Pronunciation")
        }
    }
}
