package com.example.myapplication.view.compose

import NotificationScreen
import com.example.myapplication.NotificationViewModel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
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
            ProvideAppContext(LocalContext.current) {
                CardList(navController)
            }
        }

        composable(
            "editScreen/{index}/{timestamp}/{emojiEmotion}/{primaryEmotion}/{secondaryEmotion}/{activity}/{description}",
            arguments = listOf(
                navArgument("index") { type = NavType.IntType },
                navArgument("timestamp") { type = NavType.StringType },
                navArgument("emojiEmotion") { type = NavType.IntType },
                navArgument("primaryEmotion") { type = NavType.StringType },
                navArgument("secondaryEmotion") { type = NavType.StringType },
                navArgument("activity") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val index = backStackEntry.arguments?.getInt("index") ?: 0
            val timestamp = backStackEntry.arguments?.getString("timestamp") ?: ""
            val emojiEmotion = backStackEntry.arguments?.getInt("emojiEmotion") ?: 0
            val primaryEmotion = backStackEntry.arguments?.getString("primaryEmotion") ?: ""
            val secondaryEmotion = backStackEntry.arguments?.getString("secondaryEmotion") ?: ""
            val activity = backStackEntry.arguments?.getString("activity") ?: ""
            val description = backStackEntry.arguments?.getString("description") ?: ""

            EditScreen(index = index, timestamp, emojiEmotion, primaryEmotion, secondaryEmotion, activity, description = description, navController = navController)
        }

    }}
