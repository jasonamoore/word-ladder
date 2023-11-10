package com.example.wordladder

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ChallengeAPI {
    @FormUrlEncoded
    @POST("getTodayChallenge.php")
    suspend fun getDailyChallenge(@Field("time") time : Long): Challenge

}