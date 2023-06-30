package com.example.myapplication.view.compose

import android.R.attr.checked
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.*
import com.example.myapplication.R


@Composable
fun LoginScreen(loginViewModel: LoginViewModel, navController: NavController) {

    val emailState = rememberSaveable { mutableStateOf(PreferencesHelper.email ?: "") }
    val passwordState = rememberSaveable { mutableStateOf(PreferencesHelper.password ?: "") }
    val rememberMeState = rememberSaveable { mutableStateOf(PreferencesHelper.isRememberMeChecked) }

//    val emailState = remember { mutableStateOf("") }
//    val passwordState = remember { mutableStateOf("") }
    val loginResult by loginViewModel.loginResult.observeAsState()


    val context = LocalContext.current

    val isEmailValid = emailState.value.isNotEmpty() && !isValidEmail(emailState.value)
    val isPasswordValid = passwordState.value.isNotEmpty() && passwordState.value.length < 6

    loginResult is LoginResult.Success

    if (loginResult is LoginResult.Success) {
        LaunchedEffect(loginResult) {
            Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
            navController.navigate("analyzeEmotionScreen")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        Text(
            modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.CenterHorizontally),
            text = "Hello! Sign in to your account.",
            style = MaterialTheme.typography.h6
        )

        Spacer(modifier = Modifier.height(50.dp))

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = { Text("Email") },
            isError = isEmailValid
        )

        if (isEmailValid) {
            Text(
                text = "Invalid email",
                color = Color.Red,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(26.dp))

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = isPasswordValid
        )

        if (isPasswordValid) {
            Text(
                text = "Password should be at least 6 characters long",
                color = Color.Red,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Checkbox(
                checked = rememberMeState.value,
                onCheckedChange = {
                    rememberMeState.value = it
                    PreferencesHelper.isRememberMeChecked = it
                    if (!it) {
                        PreferencesHelper.clear()
                    }
                }
                )

            Text(
                text = "Remember me",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                if (rememberMeState.value) {
                    PreferencesHelper.email = emailState.value
                    PreferencesHelper.password = passwordState.value
                }

                val email = emailState.value
                val password = passwordState.value

                if (validateInputs(email, password)) {
                    loginViewModel.login(email, password)
                }
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.teal_700)
            )
        ) {
            Text(
                text = "Log in",
                style = MaterialTheme.typography.button,
                color = Color.White
            )
        }

        TextButton(
            onClick = {
                val email = emailState.value
            if(email.isNotEmpty())
                navController.navigate("forgetPassword/$email")
                      },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Forgot password?",
                style = MaterialTheme.typography.body2,
                color = colorResource(id = R.color.teal_700)
            )
        }

        Image(
            painter = painterResource(id = R.drawable.panda),
            contentDescription = "panda",
            modifier = Modifier
                .aspectRatio(16f / 9f)
                .align(Alignment.End)
        )


        if (loginResult is LoginResult.Error) {
            val errorMessage = (loginResult as LoginResult.Error).errorMessage
            Log.d("LOGIN","Login error$errorMessage")
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