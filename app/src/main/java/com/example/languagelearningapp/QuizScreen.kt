import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.languagelearningapp.QuizQuestion

@Composable
fun QuizScreen(quizQuestions: List<QuizQuestion>, navController: NavController) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var score by remember { mutableStateOf(0) }
    var showResult by remember { mutableStateOf(false) }

    // Check if there are any questions to avoid IndexOutOfBoundsException
    val currentQuestion = if (quizQuestions.isNotEmpty()) quizQuestions[currentQuestionIndex] else null

    if (showResult) {
        ResultScreen(score = score, totalQuestions = quizQuestions.size, navController) {
            // Resetting the quiz when retrying
            currentQuestionIndex = 0
            selectedAnswer = null
            score = 0
            showResult = false
        }
    } else if (currentQuestion != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Question Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.medium // Rounded corners
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = currentQuestion.question,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Options Box
            currentQuestion.options.forEach { option ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .border(
                            width = 1.dp,
                            color = if (selectedAnswer == option) MaterialTheme.colorScheme.primary else Color.Gray,
                            shape = MaterialTheme.shapes.medium // Rounded corners
                        )
                        .selectable(
                            selected = (selectedAnswer == option),
                            onClick = { selectedAnswer = option },
                            role = Role.RadioButton
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (selectedAnswer == option),
                            onClick = { selectedAnswer = option }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = option, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (selectedAnswer == currentQuestion.correctAnswer) {
                        score++
                    }
                    if (currentQuestionIndex < quizQuestions.size - 1) {
                        currentQuestionIndex++
                        selectedAnswer = null
                    } else {
                        showResult = true
                    }
                },
                enabled = selectedAnswer != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Submit")
            }
        }
    }
}

@Composable
fun ResultScreen(score: Int, totalQuestions: Int, navController: NavController, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Quiz Completed!",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "You scored $score out of $totalQuestions",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRetry,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Retry")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("home") // Navigate to home screen
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Return Home")
        }
    }
}
