package com.example.myapplication.data.model


data class DiaryEntry(
    val emojiEmotion: String,
    val activity: String,
    val primaryEmotion: String,
    val secondaryEmotion: String,
    val description: String,
    val timestamp: String
)
