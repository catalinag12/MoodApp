package com.example.myapplication.view.compose

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.RegisterViewModel

@Composable
fun RegisterScreen(registerViewModel: RegisterViewModel, navController: NavController) {
    val nameState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val registerResult by registerViewModel.registerResult.observeAsState()

    val isEmailValid = emailState.value.isNotEmpty() && !isValidEmail(emailState.value)
    val isPasswordValid = passwordState.value.isNotEmpty() && passwordState.value.length < 6

    val context = LocalContext.current

    registerResult is RegisterResult.Success

    if (registerResult is RegisterResult.Success) {
        LaunchedEffect(registerResult) {
            Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
            navController.navigate("login") // Navigate to the login screen
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Create an account",
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = nameState.value,
            onValueChange = { nameState.value = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = isEmailValid
        )

        if (isEmailValid) {
            Text(
                text = "Invalid email",
                color = Color.Red,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = isPasswordValid
        )

        if (isPasswordValid) {
            Text(
                text = "Password should be at least 6 characters long",
                color = Color.Red,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                val name = nameState.value
                val email = emailState.value
                val password = passwordState.value
                if (validateInputs(email, password)) {
                    registerViewModel.register(email, password)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                contentColor = colorResource(id = R.color.white),
                backgroundColor = colorResource(id = R.color.teal_700)
            )
        ) {
            Text(text = "Register")
        }

        if (registerResult is RegisterResult.Error) {
            val errorMessage = (registerResult as RegisterResult.Error).errorMessage
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }
}

private fun validateInputs(email: String, password: String): Boolean {
    var isValid = true

    if (!isValidEmail(email)) {
        isValid = false
    }

    if (password.length < 6) {
        isValid = false
    }

    return isValid

}

private fun isValidEmail(email: String): Boolean {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    return email.matches(emailPattern.toRegex())
}