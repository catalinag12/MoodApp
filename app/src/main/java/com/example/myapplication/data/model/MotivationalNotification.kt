package com.example.myapplication.data.model

data class MotivationalNotification(
    var id: String = "",
    var text: String = "",
    var author: String = ""
) {
    // Default (no-argument) constructor
    constructor() : this("", "", "")
}
