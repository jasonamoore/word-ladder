package com.example.wordladder

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.InputStream
import java.util.Stack

class ScoreboardViewModel : ViewModel() {

    // make request for daily challenge JSON
    init {
        viewModelScope.launch {
            try {
                val response = ChallengeRepository.get().getTodayScoreboard()
                _scoreboardFlow.value = response
            } catch (ex : Exception) {
                Log.e("WordLadder", "Failed to load scoreboard.")
            }
        }
    }

    // flows that are updated when challenge is obtained from network request
    private val _scoreboardFlow: MutableStateFlow<Scoreboard?> = MutableStateFlow(null)
    val scoreboardFlow: StateFlow<Scoreboard?>
        get() = _scoreboardFlow.asStateFlow()

}