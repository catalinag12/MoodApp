package com.example.myapplication

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "launching"
    ) {
        composable("launching") {
            LaunchingScreen(navController)
        }
        composable("login") {
            LoginScreen()
        }
        composable("register") {
            RegisterScreen()
        }
    }
}