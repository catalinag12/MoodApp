package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun LaunchingScreen(navController: NavController){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        ) {

        Spacer(modifier = Modifier.height(100.dp))
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "panda",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "Hi! My name is Bliss and I'm gonna be your companion. Please log in or register to start your journey!",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(50.dp))


        Button(
            onClick = { navController.navigate("login") },
            enabled = true,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.teal_700)
            )
        ) {
            Text(
                text = "Log in",
                style = androidx.compose.material.MaterialTheme.typography.button,
                color = Color.White
            )
        }

        Button(
            onClick = { navController.navigate("register") },
            enabled = true,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.teal_700)
            )
        ) {
            androidx.compose.material.Text(
                text = "Register",
                style = androidx.compose.material.MaterialTheme.typography.button,
                color = Color.White
            )
        }

    }
}