package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen() {
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
            value = "",
            onValueChange = {},
            label = { Text("Email address") }
        )

        Spacer(modifier = Modifier.height(26.dp))

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = "",
            onValueChange = {},
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {},
            enabled = true,
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

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {},
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Forgot password?",
                style = MaterialTheme.typography.body2,
                color = colorResource(id = R.color.teal_700)
            )
        }

        Image(painter = painterResource(id = R.drawable.panda),
            contentDescription = "panda" ,
            modifier = Modifier.aspectRatio(16f / 9f)
        )
    }
}
