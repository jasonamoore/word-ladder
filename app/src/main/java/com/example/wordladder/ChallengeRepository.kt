package com.example.wordladder

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

const val SEC_PER_MILLISEC = 1000L
private const val SERVER_ENDPOINT = "http://ec2-18-118-19-236.us-east-2.compute.amazonaws.com/"
class ChallengeRepository private constructor() {

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
        challengeAPI.getDailyChallenge()

    suspend fun getTodayScoreboard() =
        //challengeAPI.getTodayScoreboard(System.currentTimeMillis() / SEC_PER_MILLISEC)
        challengeAPI.getTodayScoreboard()

    suspend fun postScore(name: String, score: Int) =
        challengeAPI.postScore(name, score, System.currentTimeMillis() / SEC_PER_MILLISEC)

    companion object {
        private var INSTANCE: ChallengeRepository? = null

        fun initialize() {
            if (INSTANCE == null) {
                INSTANCE = ChallengeRepository()
            }
        }

        fun get(): ChallengeRepository {
            return INSTANCE ?: throw IllegalStateException ("goober")
        }
    }

}