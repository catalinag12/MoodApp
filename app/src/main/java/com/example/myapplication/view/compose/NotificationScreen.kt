import android.app.TimePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.*
import com.example.myapplication.R
import java.util.*

@Composable
fun NotificationScreen(notificationViewModel: NotificationViewModel = viewModel(),
                       loginViewModel: LoginViewModel = viewModel(),
                       navController: NavController
) {
    val chosenHour = remember { mutableStateOf(0) }
    val context = LocalContext.current

    val logoutResult by loginViewModel.logoutResult.observeAsState()

    logoutResult is LogoutResult.Success

    if (logoutResult is LogoutResult.Success) {
        LaunchedEffect(logoutResult) {
            Toast.makeText(context, "Logout successfully", Toast.LENGTH_SHORT).show()
            navController.navigate("launching")
        }
    }

    val notificationScheduler = NotificationScheduler(context)
    var isToggledOn by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        Text(
            text = "Set notification for daily motivational quotes",
            modifier = Modifier.padding(top = 30.dp, end = 12.dp),
            style = MaterialTheme.typography.h6
        )

        Button(
            onClick = {
                val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                val currentMinute = Calendar.getInstance().get(Calendar.MINUTE)
                val timePickerDialog = TimePickerDialog(
                    context,
                    { _, hourOfDay, _ ->
                        chosenHour.value = hourOfDay
                    },
                    currentHour,
                    currentMinute,
                    true
                )
                timePickerDialog.show()
            },
            modifier = Modifier.padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.teal_700)
            )
        ) {
            Text(text = "Set Hour",
                style = MaterialTheme.typography.button,
                color = Color.White)
        }

        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.teal_700)
            ),
            onClick = {
                val hour = chosenHour.value
                notificationViewModel.testFirestoreDataRetrieval()
                notificationViewModel.scheduleDailyNotification(hour)
                    .subscribe(
                        {
                            // Daily notification scheduled successfully
                            Log.d("succes", "notificare stabilita")
                        },
                        { error ->
                            Log.d("eroare la notificare", error.message.toString())
                        }
                    )

            }
        ) {
            Text(text = "Schedule Notification",
                style = MaterialTheme.typography.button,
                color = Color.White)
        }


        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Set daily reminder",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(top = 12.dp))


        Switch(
            checked = isToggledOn,
            onCheckedChange = { isChecked ->
                isToggledOn = isChecked
                notificationViewModel.toggleNotification(isChecked)
            }
        )


        Spacer(modifier = Modifier.height(40.dp))

        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.teal_700)
            ),
            onClick = {
                loginViewModel.logout()
            }
        ) {
            Text(text = "Log out",
                style = MaterialTheme.typography.button,
                color = Color.White)
        }

    }



    var selectedItem by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {

    BottomNavigation(
        backgroundColor = colorResource(id = R.color.teal_700)
    ) {

        BottomNavigationItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint =Color.White) },
            label = { Text("Home", color = Color.White) },
            selected = selectedItem == 0,
            onClick = { selectedItem = 0
                navController.navigate("analyzeEmotionScreen")
            },
        )

        BottomNavigationItem(
            icon = { Icon(Icons.Default.MenuBook, contentDescription = "Diary", tint =Color.White) },
            label = { Text("Diary", color = Color.White) },
            selected = selectedItem == 1,
            onClick = { selectedItem = 1
                navController.navigate("diaryScreen")
            },
        )

        BottomNavigationItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings", tint =Color.White) },
            label = { Text("Settings", color = Color.White) },
            selected = selectedItem == 2,
            onClick = {
                selectedItem = 2
            },
        )
    }
    }
}
