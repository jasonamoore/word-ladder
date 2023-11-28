package com.example.wordladder

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChallengeAPI {

    @GET("/getTodayChallenge.php")
    suspend fun getDailyChallenge(): Challenge

    @GET("/getTodayScoreboard.php")
    suspend fun getTodayScoreboard(): Scoreboard

    @FormUrlEncoded
    @POST("/postScore.php")
    suspend fun postScore(@Field("name") name : String,
                          @Field("score") score : Int,
                          @Field("time") time: Long)

}