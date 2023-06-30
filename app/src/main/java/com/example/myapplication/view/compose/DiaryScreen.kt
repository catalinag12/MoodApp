package com.example.myapplication.view.compose

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class CardItem(
    val imageResource: Int,
    val emoji: String,
    val date: LocalDate,
    val emotionalState: String,
    val activities: List<String>,
    val description: String,
    val primary: String
)

@RequiresApi(Build.VERSION_CODES.O)
val cardItems = listOf(
    CardItem(
        imageResource = R.drawable.happy,
        emoji = "😄",
        date = LocalDate.now(),
        emotionalState = "Happy",
        activities = listOf("Picnic", "Walk in the park"),
        description = "Good day",
        primary = "Happiness"
    ),
    CardItem(
        imageResource = R.drawable.sad,
        emoji = "😢",
        date = LocalDate.now().minusDays(1),
        emotionalState = "Sad",
        activities = listOf("Exam", "Hard work"),
        description = "Hard and tiring day",
        primary = "Sadness"
    )
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CardList(
    navController: NavController
) {
    LazyColumn {
        items(cardItems) { cardItem ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable { /* Handle card click event */ },
                elevation = 4.dp
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = cardItem.imageResource),
                        contentDescription = null,
                        modifier = Modifier
                            .size(96.dp)
                            .clip(shape = MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = cardItem.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                            style = MaterialTheme.typography.subtitle1
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = cardItem.emotionalState,
                            style = MaterialTheme.typography.subtitle2,
                            color = colorResource(id = R.color.teal_700)
                        )
                        Text(
                            text = "Primary emotion: ${cardItem.primary}",
                            style = MaterialTheme.typography.subtitle2,
                            color = colorResource(id = R.color.teal_700)
                        )
                        Text(
                            text = "Secondary emotion: Unknown",
                            style = MaterialTheme.typography.subtitle2,
                            color = colorResource(id = R.color.teal_700)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = cardItem.activities.joinToString(", "),
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.secondaryVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = cardItem.description,
                            style = MaterialTheme.typography.body2,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = { /* Handle edit button click event */ },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colors.secondary
                        )
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        var selectedItem by remember { mutableStateOf(0) }

        BottomNavigation(
            backgroundColor = colorResource(id = R.color.teal_700)
        ) {

            BottomNavigationItem(
                icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint =Color.White) },
                label = { Text("Home", color = Color.White) },
                selected = selectedItem == 0,
                onClick = { selectedItem = 0
                    navController.navigate("notificationScreen")
                }
            )

            BottomNavigationItem(
                icon = { Icon(Icons.Default.MenuBook, contentDescription = "Diary", tint =Color.White) },
                label = { Text("Diary", color = Color.White) },
                selected = selectedItem == 1,
                onClick = { selectedItem = 1 }
            )

            BottomNavigationItem(
                icon = { Icon(Icons.Default.Settings, contentDescription = "Settings", tint =Color.White) },
                label = { Text("Settings", color = Color.White) },
                selected = selectedItem == 2,
                onClick = {
                    selectedItem = 2
                    navController.navigate("notificationScreen")
                }
            )
        }

        FloatingActionButton(
            onClick = { /* Handle sorting button click event */ },
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
