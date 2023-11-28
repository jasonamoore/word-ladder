package com.example.wordladder

import android.app.Application

class WordLadderApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ChallengeRepository.initialize()
    }
}