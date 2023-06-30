package com.example.myapplication.view.compose

import NotificationScreen
import com.example.myapplication.NotificationViewModel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.*
import com.example.myapplication.data.model.AnalyzeEmotionViewModel

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun NavGraph(loginViewModel: LoginViewModel,
             registerViewModel: RegisterViewModel,
             forgetPasswordViewModel: ForgetPasswordViewModel,
             analyzeEmotionViewModel: AnalyzeEmotionViewModel,
             notificationViewModel: NotificationViewModel,
             diaryViewModel: DiaryViewModel
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "launching"
    ) {
        composable("launching") {
            LaunchingScreen(navController)
            //com.example.myapplication.view.compose.CardList()
            //AnalyzeEmotionScreen(analyzeEmotionViewModel, navController)
        }
        composable("login") {
            LoginScreen(loginViewModel, navController)
        }
        composable("register") {
            RegisterScreen(registerViewModel, navController)
        }
        composable(
            "forgetPassword/{email}",
            arguments = listOf(navArgument("email") {
                type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            ResetPasswordScreen(forgetPasswordViewModel, navController, email)
        }
        composable("analyzeEmotionScreen") {
            AnalyzeEmotionScreen(analyzeEmotionViewModel,  diaryViewModel, navController)
        }
        composable("notificationScreen") {
            NotificationScreen(notificationViewModel, loginViewModel, navController)
        }
        composable("diaryScreen") {
            CardList(navController)
        }
}}
