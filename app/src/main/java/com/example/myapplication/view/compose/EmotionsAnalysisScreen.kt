package com.example.myapplication.view.compose

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.DiaryViewModel
import com.example.myapplication.R
import com.example.myapplication.data.model.AnalyzeEmotionViewModel
import com.example.myapplication.data.model.DiaryEntry
import com.example.myapplication.data.model.EmotionAnalysisResult
import com.microsoft.projectoxford.face.contract.Emotion
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.security.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun AnalyzeEmotionScreen(
    viewModel: AnalyzeEmotionViewModel = viewModel(),
    diaryViewModel: DiaryViewModel = viewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val selectedImage: ImageBitmap? by viewModel.selectedImage

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewModel.setSelectedImageFromUri(context, it)
            viewModel.analyzeEmotionFromImage(getImageBytesFromUri(context, it))
        }
    }

    var emojiEmotion by remember { mutableStateOf("") }
    var activity by remember { mutableStateOf("") }
    var primaryEmotionSaved by remember { mutableStateOf("") }
    var secondaryEmotionSaved by remember { mutableStateOf("") }
    var description by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "What is your mood today?",
            style = MaterialTheme.typography.h6,
            color = colorResource(id = R.color.teal_700),
            modifier = Modifier.align(CenterHorizontally)
        )
        Row(modifier = Modifier.padding(horizontal =47.dp)) {
            EmotionImage(painterResource(R.drawable.happy), "Image 1") {
                emojiEmotion = "Happy"
            }
            EmotionImage(painterResource(R.drawable.sad), "Image 2") {
                emojiEmotion = "Sad"
            }
            EmotionImage(painterResource(R.drawable.verysad), "Image 3") {
                emojiEmotion = "Very Sad"
            }
            EmotionImage(painterResource(R.drawable.angry), "Image 4") {
                emojiEmotion = "Angry"
            }
        }
        Row(modifier = Modifier.padding(horizontal = 80.dp)) {
            EmotionImage(painterResource(R.drawable.pleased), "Image 5") {
                emojiEmotion = "Pleased"
            }
            EmotionImage(painterResource(R.drawable.pissed), "Image 6") {
                emojiEmotion = "Pissed"
            }
            EmotionImage(painterResource(R.drawable.indifferent), "Image 7") {
                emojiEmotion = "Indifferent"
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Check your mood by adding a picture",
            style = MaterialTheme.typography.h6,
            color = colorResource(id = R.color.teal_700),
            modifier = Modifier.align(CenterHorizontally)
        )

        Button(
            onClick = { launcher.launch("image/*") },
            modifier = Modifier.align(CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.teal_700)
            )
        ) {
            Text(text = "Select Image", color = Color.White)
        }
        Spacer(modifier = Modifier.height(30.dp))

        selectedImage?.let { imageBitmap ->
            Column(
                horizontalAlignment = CenterHorizontally
            ) {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    contentScale = ContentScale.FillBounds
                )

                Spacer(modifier = Modifier.height(10.dp))

                EmotionAnalysisResult(
                    viewModel = viewModel,
                    onSavePrimaryEmotion = { primaryEmotionSaved = it },
                    onSaveSecondaryEmotion = { secondaryEmotionSaved = it })
            }
            Spacer(modifier = Modifier.height(30.dp))
        }


        Text(
            text = "What activities did you have today?",
            style = MaterialTheme.typography.h6,
            color = colorResource(id = R.color.teal_700),
            modifier = Modifier.align(CenterHorizontally)
        )

        Row(modifier = Modifier.padding(horizontal = 40.dp)) {
            EmotionImage(painterResource(R.drawable.bowling), "Image 1") {
                activity = "Bowling"
            }
            EmotionImage(painterResource(R.drawable.job), "Image 2") {
                activity = "Job"
            }
            EmotionImage(painterResource(R.drawable.sport), "Image 3") {
                activity = "Sport"
            }
            EmotionImage(painterResource(R.drawable.read), "Image 4") {
                activity = "Read"
            }
        }
        Row(modifier = Modifier.padding(horizontal = 66.dp)) {
            EmotionImage(painterResource(R.drawable.sleep), "Image 5") {
                activity = "Sleep"
            }
            EmotionImage(painterResource(R.drawable.phone), "Image 6") {
                activity = "Phone"
            }
            EmotionImage(painterResource(R.drawable.games), "Image 7") {
                activity = "Games"
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Add a description",
            style = MaterialTheme.typography.h6,
            color = colorResource(id = R.color.teal_700),
            modifier = Modifier.align(CenterHorizontally)
        )
        val textState = remember { mutableStateOf("") }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(color = Color.LightGray, shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            TextField(
                value = textState.value,
                onValueChange = { textState.value = it },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.body1,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.LightGray, // Custom text field color
                    cursorColor = Color.Black
                )
            )
            // Update the description mutable state variable with the user input
            description = TextFieldValue(textState.value)
        }

        Button(
            onClick = {
                // Handle save logic
                val date: Date = com.google.firebase.Timestamp.now().toDate()
                val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val formattedDate: String = dateFormat.format(date)

                diaryViewModel.saveDiaryEntry(
                    DiaryEntry(
                        emojiEmotion,
                        activity,
                        primaryEmotionSaved,
                        secondaryEmotionSaved,
                        description.text,
                        formattedDate
                )
                )
            },
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.teal_700)
            )
        ) {
            Text(text = "Add to journal", color = Color.White)
        }

        Spacer(modifier = Modifier.height(50.dp))


    }
    var selectedItem by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        BottomNavigation(
            backgroundColor = colorResource(id = R.color.teal_700)
        ) {
            BottomNavigationItem(
                icon = {
                    Icon(
                        Icons.Default.Home,
                        contentDescription = "Home",
                        tint = Color.White
                    )
                },
                label = { Text("Home", color = Color.White) },
                selected = selectedItem == 0,
                onClick = { selectedItem = 0 }
            )

            BottomNavigationItem(
                icon = {
                    Icon(
                        Icons.Default.MenuBook,
                        contentDescription = "Diary",
                        tint = Color.White
                    )
                },
                label = { Text("Diary", color = Color.White) },
                selected = selectedItem == 1,
                onClick = { selectedItem = 1
                    navController.navigate("diaryScreen")
                }
            )

            BottomNavigationItem(
                icon = {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color.White
                    )
                },
                label = { Text("Settings", color = Color.White) },
                selected = selectedItem == 2,
                onClick = {
                    selectedItem = 2
                    navController.navigate("notificationScreen")
                }
            )
        }
    }
}

@Composable
fun EmotionImage(
    painter: Painter,
    contentDescription: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
            .size(60.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
@Composable
private fun EmotionAnalysisResult(viewModel: AnalyzeEmotionViewModel, onSavePrimaryEmotion: (String) -> Unit, onSaveSecondaryEmotion: (String) -> Unit) {
    val result by remember { derivedStateOf { viewModel.emotionResult } }

    when (result) {
        is EmotionAnalysisResult.Loading -> {

                CircularProgressIndicator(
                    color = colorResource(id = R.color.teal_700)
                )
        }
        is EmotionAnalysisResult.Success -> {
            val resultSuccess = result as EmotionAnalysisResult.Success
            val primaryEmotion = resultSuccess.primaryEmotion
            val secondaryEmotion = resultSuccess.secondaryEmotion

            val selectedPrimaryEmotion by remember { mutableStateOf(primaryEmotion) }

            onSavePrimaryEmotion(selectedPrimaryEmotion)
            onSaveSecondaryEmotion(secondaryEmotion)

            Text(
                text = "Primary emotion: $selectedPrimaryEmotion",
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Secondary emotion: $secondaryEmotion",
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        is EmotionAnalysisResult.Error -> {
            val errorText = (result as EmotionAnalysisResult.Error).errorMessage
            Text(text = errorText)
        }
        else -> {}
    }
}



@Throws(IOException::class)
private fun getImageBytesFromUri(context: Context, uri: Uri): ByteArray {
    val inputStream = context.contentResolver.openInputStream(uri)
    val outputStream = ByteArrayOutputStream()
    inputStream?.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }
    return outputStream.toByteArray()
}

private fun isSecondaryEmotionSignificant(primaryEmotion: Emotion, secondaryEmotion: Emotion): Boolean {
    val primaryScore = getEmotionScore(primaryEmotion)
    val secondaryScore = getEmotionScore(secondaryEmotion)
    val threshold = 0.8 // Adjust the threshold value as per your requirement

    return secondaryScore >= threshold * primaryScore
}

private fun getEmotionScore(emotion: Emotion): Double {
    return emotion.anger + emotion.contempt + emotion.disgust +
            emotion.fear + emotion.happiness + emotion.neutral +
            emotion.sadness + emotion.surprise
}