package rs.raf.catlist.quiz.ui

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
import rs.raf.catlist.quiz.model.QuizQuestion

fun NavGraphBuilder.quiz(
    route: String,
    onQuizCompleted: () -> Unit,
    onClose: () -> Unit
) = composable(
    route = route,
    enterTransition = { slideInVertically { it } },
    popExitTransition = { slideOutVertically { it } },
) { navBackStackEntry ->

    val kvizViewModel: QuizViewModel = hiltViewModel(navBackStackEntry)

    val state = kvizViewModel.state.collectAsState()

    QuizScreen(
        state = state.value,
        eventPublisher = {
            kvizViewModel.setEvent(it)
        },
        onOptionSelected = { option -> kvizViewModel.submitAnswer(option) },
        onQuizCompleted = onQuizCompleted,
        onClose = onClose
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    state: QuizContract.QuizState,
    eventPublisher: (uiEvent: QuizContract.QuizEvent) -> Unit,
    onOptionSelected: (String) -> Unit,
    onQuizCompleted: () -> Unit,
    onClose: () -> Unit
) {
    if (state.showExitDialog) {
        AlertDialog(
            onDismissRequest = { eventPublisher(QuizContract.QuizEvent.StopQuiz) },
            title = { Text("Exit Quiz?") },
            text = { Text("Are you sure you want to exit the quiz?") },
            confirmButton = {
                Button(
                    onClick = { onClose() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = Color.Black,

                        ),
                    border = BorderStroke(1.dp, Color.Black)
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = { eventPublisher(QuizContract.QuizEvent.ContinueQuiz) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = Color.Black,
                    ),
                    border = BorderStroke(1.dp, Color.Black)
                ) {
                    Text("No")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Quiz",
                        style = TextStyle(
                            fontSize = 27.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { eventPublisher(QuizContract.QuizEvent.StopQuiz) }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },

                actions = {
                    Text(
                        text = "Time : ${state.timeRemaining / 1000 / 60} : ${state.timeRemaining / 1000 % 60} ",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                },colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFC4BFAE)
                )
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                if (state.loading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (state.questions.isNotEmpty()) {
                    Crossfade(targetState = state.currentQuestionIndex, label = "") { index ->
                        val question = state.questions[index]
                        QuestionScreen(
                            state = state,
                            question = question,
                            onOptionSelected = onOptionSelected
                        )
                    }
                } else if (state.quizFinished){
                    ResultScreen(
                        score = state.score,
                        onFinish = onClose,
                        eventPublisher = { eventPublisher(it) }
                    )
                } else {
                    onQuizCompleted()
                    Log.d("QUIZ", "No questions available")
                }
            }
        }
    )
}

@Composable
fun QuestionScreen(
    state: QuizContract.QuizState,
    question: QuizQuestion,
    onOptionSelected: (String) -> Unit
) {
    val kvizViewModel: QuizViewModel = hiltViewModel()
    val state = kvizViewModel.state.collectAsState().value

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        BackHandler {
            kvizViewModel.setEvent(QuizContract.QuizEvent.StopQuiz)
        }

        Text(
            text = (state.currentQuestionIndex + 1).toString() + " / 20",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            ),
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )

        Text(
            text = question.question,
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            ),
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )


        val optionskviz = mutableListOf<String>()
        optionskviz.add(question.options[0].split('|')[0])
        optionskviz.add(question.options[1].split('|')[0])

        val photosQuiz = mutableListOf<String>()
        photosQuiz.add(question.options[0].split('|')[1])
        photosQuiz.add(question.options[1].split('|')[1])

        if (question.correctImageUrl != null && question.inCorrectImageUrl != null) {
            Row  {
                Box(
                    modifier = Modifier
                        .width(175.dp)
                        .height(230.dp)
                        .clip(CutCornerShape(5.dp))
                        .border(2.dp, Color.Black)
                        .padding(4.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(photosQuiz[0]),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.Center)
//                            .fillMaxSize()
                            .border(2.dp, Color.Transparent, RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop,
                    )
                }
                Box(
                    modifier = Modifier
                        .width(175.dp)
                        .height(230.dp)
                        .clip(CutCornerShape(5.dp))
                        .border(2.dp, Color.Black)
                        .padding(4.dp)

                ) {
                    Image(
                        painter = rememberAsyncImagePainter(photosQuiz[1]),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.Center)
//                            .fillMaxSize()
                            .border(2.dp, Color.Transparent, RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop,
                    )
                }
            }

        }
        Row(

        ) {
            val options = optionskviz
            options.forEach { option ->
                val buttonColor = when {
                    state.selectedOption == null -> MaterialTheme.colorScheme.surface
                    option == state.selectedOption && option == state.questions[state.currentQuestionIndex].correctAnswer -> Color.Green
                    option == state.selectedOption && option != state.questions[state.currentQuestionIndex].correctAnswer -> Color.Red
                    option != state.selectedOption && option == state.questions[state.currentQuestionIndex].correctAnswer -> Color.Green
                    else -> MaterialTheme.colorScheme.surface
                }
                Button(
                    onClick = {
                        if (state.selectedOption == null) onOptionSelected(option)
                    },

                    modifier = Modifier
//                        .fillMaxWidth()
                        .width(175.dp)
                        .padding(horizontal = 10.dp)

                        .height(60.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor,
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(3.dp, Color.Black)
                ) {
                    Text(
                        text = option,
                        style = TextStyle(
                            fontSize = 16.sp,

                            ),
                        modifier = Modifier


                            .align(Alignment.CenterVertically)






                    )



                }
            }
        }


    }
}

@Composable
fun ResultScreen(
    score: Float,
    onFinish: () -> Unit,
    eventPublisher: (uiEvent: QuizContract.QuizEvent) -> Unit,
) {
    Scaffold(
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(bottom = 100.dp)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "Your Score: $score",
                        style = TextStyle(
                            fontSize = 35.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                        ),
                        modifier = Modifier.padding(bottom = 30.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = {
                                eventPublisher(QuizContract.QuizEvent.PublishScore(score))
                                onFinish()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = Color.Black,
                            ),
                            border = BorderStroke(1.dp, Color.Black),
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(
                                text = "Publish",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 2.sp,
                                ),
                            )
                        }
                        Button(
                            onClick = {
                                eventPublisher(QuizContract.QuizEvent.FinishQuiz)
                                onFinish()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = Color.Black,
                            ),
                            border = BorderStroke(1.dp, Color.Black),
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(
                                text = "Finish",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 2.sp,
                                ),
                            )
                        }
                    }
                }
            }
        }
    )
}