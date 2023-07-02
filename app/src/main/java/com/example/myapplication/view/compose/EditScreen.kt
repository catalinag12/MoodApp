package com.example.myapplication.view.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.model.DiaryEntry
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun EditScreen(
    index: Int,
    timestamp: String,
    emojiEmotion:Int,
    primaryEmotion:String,
    secondaryEmotion: String,
    activity: String,
    description: String,
    navController: NavController
) {
    val context = LocalContext.current

    var descriptionState = remember { mutableStateOf(TextFieldValue(description)) }

    val firestore = FirebaseFirestore.getInstance()

    // Function to save the edited data to Firebase
    fun saveData() {
        val updatedDescription = descriptionState.value.text

        val entry = DiaryEntry(
             index,
            emojiEmotion,activity,primaryEmotion,secondaryEmotion,
            updatedDescription, timestamp
        )

        firestore.collection("diaryEntries")
            .document(entry.id.toString())
            .set(entry)
            .addOnSuccessListener {
                // Entry saved or updated successfully
                navController.popBackStack()
            }
            .addOnFailureListener { error ->
                // Error saving or updating entry
                // Handle the error as needed
            }
    }




    // Function to delete the entry from Firebase
    fun deleteEntry() {
        firestore.collection("diaryEntries")
            .document(index.toString())
            .delete()
            .addOnSuccessListener {
                // Entry deleted successfully
                navController.popBackStack()
            }
            .addOnFailureListener { error ->
                // Error deleting entry
                // Handle the error as needed
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Add more details about your feelings",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(46.dp))

        TextField(
            value = descriptionState.value,
            onValueChange = { descriptionState.value = it },
            label = { Text("Description") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column {
            Button(
                onClick = { saveData() },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = R.color.teal_700),
                    contentColor = colorResource(id = R.color.white)
                )
            ) {
                Icon(Icons.Default.Save, contentDescription = "Save")
                Text("Save changes")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { deleteEntry() },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = R.color.teal_700),
                    contentColor = colorResource(id = R.color.white)
                )
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
                Text("Delete entry")
            }
        }
    }
}
