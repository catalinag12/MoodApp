package com.example.myapplication.view.compose

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.ForgetPasswordViewModel
import com.example.myapplication.R
import com.example.myapplication.ResetPasswordResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    resetPasswordViewModel: ForgetPasswordViewModel,
    navController: NavController,
    emailFromLogin: String
) {

    var email by remember { mutableStateOf(emailFromLogin) }


    val context = LocalContext.current

    val resetPasswordResult by resetPasswordViewModel.resetPasswordResult.observeAsState()

    resetPasswordResult is ResetPasswordResult.Success

    if (resetPasswordResult is ResetPasswordResult.Success) {
        LaunchedEffect(resetPasswordResult) {
            Toast.makeText(context, "The link for resetting the password was sent. Please check your email.", Toast.LENGTH_SHORT).show()
            navController.navigate("login") // Navigate to the login screen
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Forget Password",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(16.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { resetPasswordViewModel.resetPassword(email) },
            modifier = Modifier.padding(horizontal = 16.dp),
            colors =ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.teal_700),
                contentColor = colorResource(id = R.color.white)
            )
        ) {
            Text(text = "Reset Password")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {navController.navigate("login")},
            modifier = Modifier.align(Alignment.CenterHorizontally),
            colors =ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.white),
                contentColor = colorResource(id = R.color.teal_700)
            )
        ) {
            Text(text = "Back to Login")
        }

        if (resetPasswordResult is ResetPasswordResult.Error) {
            val errorMessage = (resetPasswordResult as ResetPasswordResult.Error).errorMessage
            //Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }
}