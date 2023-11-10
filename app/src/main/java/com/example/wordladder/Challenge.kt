package com.example.wordladder

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.text.SimpleDateFormat

@JsonClass(generateAdapter = true)
data class Challenge (
        @Json(name = "day") val date: String,
        @Json(name = "start") var startWord: String,
        @Json(name = "end") var endWord: String
    ) {

    init { // force convert retrieved data to uppercase
        startWord = startWord.uppercase()
        endWord = endWord.uppercase()
    }

}