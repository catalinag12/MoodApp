package com.example.myapplication.data.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.microsoft.projectoxford.face.FaceServiceClient
import com.microsoft.projectoxford.face.FaceServiceRestClient
import com.microsoft.projectoxford.face.contract.Emotion
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream

sealed class EmotionAnalysisResult {
    object Loading : EmotionAnalysisResult()
    data class Success(val primaryEmotion: String, val secondaryEmotion: String) : EmotionAnalysisResult()
    data class Error(val errorMessage: String) : EmotionAnalysisResult()
}

class AnalyzeEmotionViewModel(private val faceServiceClient: FaceServiceRestClient) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val selectedImage = mutableStateOf<ImageBitmap?>(null)
    var emotionResult by mutableStateOf<EmotionAnalysisResult>(EmotionAnalysisResult.Loading)
        private set


    fun setSelectedImage(image: ImageBitmap) {
        selectedImage.value = image
    }


    fun analyzeEmotionFromImage(imageBytes: ByteArray) {
        val inputStream = ByteArrayInputStream(imageBytes)

        emotionResult = EmotionAnalysisResult.Loading

        val detectEmotionDisposable = Single.fromCallable {
            faceServiceClient.detect(
                inputStream,
                true,
                false,
                arrayOf(FaceServiceClient.FaceAttributeType.Emotion)
            )
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ detectedFaces ->
                emotionResult = if (detectedFaces.isNotEmpty()) {
                    val primaryEmotion = getPrimaryAndSecondaryEmotions(detectedFaces.first().faceAttributes.emotion)
                    val emotionText = "Primary emotion: $primaryEmotion"
                    EmotionAnalysisResult.Success(primaryEmotion.first, primaryEmotion.second)
                } else {
                    EmotionAnalysisResult.Error("No faces found in the image.")
                }
            }, { error ->
                emotionResult = EmotionAnalysisResult.Error("Error analyzing emotion: ${error.message}")
                Log.e("emotie", error.message.toString())
            })

        compositeDisposable.add(detectEmotionDisposable)
    }

    private fun getPrimaryAndSecondaryEmotions(emotion: Emotion): Pair<String, String> {
        val emotionMap = mapOf(
            "anger" to emotion.anger,
            "contempt" to emotion.contempt,
            "disgust" to emotion.disgust,
            "fear" to emotion.fear,
            "happiness" to emotion.happiness,
            "neutral" to emotion.neutral,
            "sadness" to emotion.sadness,
            "surprise" to emotion.surprise
        )

        val sortedEmotions = emotionMap.filterValues { value -> value > 0.4 }
            .toList()
            .sortedByDescending { (_, value) -> value }
        val primaryEmotion = sortedEmotions.first().first
        val secondaryEmotion = sortedEmotions.getOrNull(1)?.first ?: "Unknown"

        return Pair(primaryEmotion.capitalize(), secondaryEmotion.capitalize())
    }



    @RequiresApi(Build.VERSION_CODES.N)
    fun setSelectedImageFromUri(context: Context, uri: Uri) {
        val rotation = getImageRotation(context, uri)
        val bitmap = loadImageBitmapFromUri(context, uri)
        val rotatedBitmap = bitmap?.let { rotateBitmap(it, rotation) }
        rotatedBitmap?.let { setSelectedImage(it.asImageBitmap()) }
    }


    private fun loadImageBitmapFromUri(context: Context, uri: Uri): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            Log.e("AnalyzeEmotionScreen", "Failed to load image bitmap from URI: ${e.message}")
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getImageRotation(context: Context, uri: Uri): Int {
        val exifInterface = context.contentResolver.openInputStream(uri)?.use {
            ExifInterface(it)
        }
        return when (exifInterface?.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, rotation: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(rotation.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }


    fun saveEmotion(description: String) {
        viewModelScope.launch {
           // emotionsRepository.saveEmotion(description)
        }
    }



}
