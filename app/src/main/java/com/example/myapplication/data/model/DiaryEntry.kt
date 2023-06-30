package com.example.myapplication.data.model

import com.google.firebase.Timestamp


data class DiaryEntry(
    val emojiEmotion: Int? = null,
    val activity: String? = null,
    val primaryEmotion: String? = null,
    val secondaryEmotion: String? = null,
    val description: String? = null,
    val timestamp: String? = null
)
 {
    constructor() : this(null, null, null, null, null, null)
}
