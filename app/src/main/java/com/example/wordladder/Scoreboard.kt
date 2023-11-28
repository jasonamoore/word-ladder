package com.example.wordladder

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Scoreboard (
    @Json(name = "names") val nameList: List<String>,
    @Json(name = "scores") var scoreList: List<Int>
)