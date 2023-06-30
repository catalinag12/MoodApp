package com.example.myapplication.di

import com.example.myapplication.LoginViewModel
import com.example.myapplication.NotificationViewModel
import com.example.myapplication.*
import com.example.myapplication.data.model.AnalyzeEmotionViewModel
import com.example.myapplication.firebase.FirebaseAuthService
import com.example.myapplication.repository.DiaryRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.microsoft.projectoxford.face.FaceServiceRestClient
import dagger.Component
import dagger.Module
import dagger.Provides


@Module
class ViewModelModule {
    @Provides
    fun provideLoginViewModel(firebaseAuthService: FirebaseAuthService): LoginViewModel {
        return LoginViewModel(firebaseAuthService)
    }

    @Provides
    fun provideRegisterViewModel(firebaseAuthService: FirebaseAuthService): RegisterViewModel {
        return RegisterViewModel(firebaseAuthService)
    }

    @Provides
    fun provideForgetPasswordViewModel(firebaseAuthService: FirebaseAuthService): ForgetPasswordViewModel {
        return ForgetPasswordViewModel(firebaseAuthService)
    }

    @Provides
    fun provideNotificationViewModel(): NotificationViewModel {
        return NotificationViewModel()
    }

    @Provides
    fun provideFirebaseAuthService(): FirebaseAuthService{
        return  FirebaseAuthService()
    }

    @Provides
    fun provideFaceServiceClient(): FaceServiceRestClient {
        return FaceServiceRestClient( "https://emotionsapp.cognitiveservices.azure.com//face/v1.0/", "285996a59c1e479ea593485037c573a6")
    }

    @Provides
    fun provideAnalyzeEmotionViewModel(faceServiceClient: FaceServiceRestClient): AnalyzeEmotionViewModel {
        return AnalyzeEmotionViewModel(faceServiceClient)
    }

    @Provides
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    fun provideDiaryRepository(firestore: FirebaseFirestore): DiaryRepository {
        return DiaryRepository(firestore)
    }

    @Provides
    fun provideDiaryViewModel(repository: DiaryRepository): DiaryViewModel {
        return DiaryViewModel(repository)
    }

}

@Component(modules = [ViewModelModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)

    fun inject(receiver: MyNotificationReceiver)

}



