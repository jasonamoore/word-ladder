package com.example.wordladder

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

const val SEC_PER_MILLISEC = 1000L
private const val SERVER_ENDPOINT = "http://ec2-18-118-19-236.us-east-2.compute.amazonaws.com/"
class ChallengeRepository {

    private val challengeAPI: ChallengeAPI

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(SERVER_ENDPOINT)
            //.addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        challengeAPI = retrofit.create()
    }

    suspend fun getDailyChallenge() =
        challengeAPI.getDailyChallenge(System.currentTimeMillis() / SEC_PER_MILLISEC)

}