package com.example.myapplication.view.compose

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.model.DiaryEntry
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val LocalAppContext = staticCompositionLocalOf<android.content.Context> {
    error("No app context provided")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CardList(
    navController: NavController
) {
    var diaryEntries by remember { mutableStateOf(emptyList<DiaryEntry>()) }
    var sortedEntries by remember { mutableStateOf(emptyList<DiaryEntry>()) }
    var isSorted by remember { mutableStateOf(false) }

    val appContext = LocalAppContext.current

    LaunchedEffect(Unit) {
        fetchDiaryEntriesFromFirestore()?.let { fetchedEntries ->
            diaryEntries = fetchedEntries
        }
    }

    if (isSorted) {
        sortedEntries = sortDiaryEntriesByTimestamp(diaryEntries)
    }

    LazyColumn(modifier = Modifier.padding(bottom = 130.dp)) {
        itemsIndexed(if (isSorted) sortedEntries else diaryEntries) { index, cardItem ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable { /* Handle card click event */ },
                elevation = 4.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 12.dp)
                ) {
                    cardItem.emojiEmotion?.let {
                        painterResource(id = cardItem.emojiEmotion)
                    }?.let {
                        Image(
                            painter = it,
                            contentDescription = null,
                            modifier = Modifier
                                .size(96.dp)
                                .clip(shape = MaterialTheme.shapes.medium),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            cardItem.timestamp?.let {
                                Text(
                                    text = cardItem.timestamp.toString(),
                                    style = MaterialTheme.typography.subtitle1
                                )
                            }
                            Spacer(modifier = Modifier.width(20.dp))
                            IconButton(
                                onClick = {
                                    navController.navigate("editScreen/$index/${cardItem.timestamp}/${cardItem.emojiEmotion}/${cardItem.primaryEmotion}/${cardItem.secondaryEmotion}/${cardItem.activity}/${cardItem.description}")
                                },
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = MaterialTheme.colors.secondary
                                )
                            }
                        }
                        Text(
                            text = "Primary emotion: ${cardItem.primaryEmotion}",
                            style = MaterialTheme.typography.subtitle2,
                            color = colorResource(id = R.color.teal_700)
                        )
                        Text(
                            text = "Secondary emotion: ${cardItem.secondaryEmotion}",
                            style = MaterialTheme.typography.subtitle2,
                            color = colorResource(id = R.color.teal_700)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        cardItem.activity?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.secondaryVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        cardItem.description?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.body2,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }

    Spacer(modifier = Modifier.width(100.dp))


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        var selectedItem by remember { mutableStateOf(0) }

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
                onClick = {
                    selectedItem = 0
                    navController.navigate("analyzeEmotionScreen")
                }
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
                onClick = { selectedItem = 1 }
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



        FloatingActionButton(
            onClick = {
                isSorted = !isSorted
            },
            backgroundColor = colorResource(id = R.color.teal_700),
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier.padding(bottom = 80.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Sort,
                contentDescription = "Sort",
                tint = Color.White
            )
        }


    }
}

private suspend fun fetchDiaryEntriesFromFirestore(): List<DiaryEntry>? {
    return try {
        val firestore = FirebaseFirestore.getInstance()
        val snapshot = firestore.collection("diaryEntries").get().await()
        val entries = snapshot.toObjects(DiaryEntry::class.java)
        entries
    } catch (e: Exception) {
        Log.d("FETCHING FROM FIREBASE", "Failed to fetch diaryEntries", e)
        null
    }
}


@Composable
fun ProvideAppContext(
    context: android.content.Context,
    content: @Composable () -> Unit
) {
    val appContext = remember(context) { context.applicationContext }
    CompositionLocalProvider(LocalAppContext provides appContext) {
        content()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun sortDiaryEntriesByTimestamp(entries: List<DiaryEntry>): List<DiaryEntry> {
    Log.d("SORTING", "Sorting function called")
    return entries.sortedBy { entry ->
        LocalDate.parse(entry.timestamp, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    }
}





