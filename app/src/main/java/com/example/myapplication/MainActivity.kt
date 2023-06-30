package com.example.myapplication

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.model.AnalyzeEmotionViewModel
import com.example.myapplication.di.AppComponent
import com.example.myapplication.di.DaggerAppComponent
import com.example.myapplication.view.compose.NavGraph
import javax.inject.Inject

class MainActivity : AppCompatActivity() {


    @Inject
    lateinit var loginViewModel: LoginViewModel

    @Inject
    lateinit var registerViewModel: RegisterViewModel

    @Inject
    lateinit var forgetPasswordViewModel: ForgetPasswordViewModel

    @Inject
    lateinit var analyzeEmotionViewModel: AnalyzeEmotionViewModel

    @Inject
    lateinit var notificationViewModel: NotificationViewModel

    @Inject
    lateinit var diaryViewModel: DiaryViewModel

    private lateinit var appComponent: AppComponent


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        PreferencesHelper.init(this)

        appComponent = DaggerAppComponent.create()
        appComponent.inject(this)

        notificationViewModel.initialize(applicationContext)

        setContent {
            NavGraph(loginViewModel,
            registerViewModel,
            forgetPasswordViewModel,
            analyzeEmotionViewModel,
            notificationViewModel,
            diaryViewModel)
        }

    }
}
