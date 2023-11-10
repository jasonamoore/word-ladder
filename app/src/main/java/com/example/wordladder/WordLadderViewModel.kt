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


const val WORD_SIZE = 4

class WordLadderViewModel : ViewModel() {

    // make request for daily challenge JSON
    init {
        viewModelScope.launch {
            try {
                val response = ChallengeRepository().getDailyChallenge()
                _challengeFlow.value = response
            } catch (ex : Exception) {
                Log.e("WordLadder", "Failed to load daily challenge.")
            }
        }
    }

    // flows that are updated when challenge is obtained from network request
    private val _challengeFlow: MutableStateFlow<Challenge?> = MutableStateFlow(null)
    val challengeFlow: StateFlow<Challenge?>
        get() = _challengeFlow.asStateFlow()

    // the current Challenge object, null when not yet loaded
    var currentChallenge: Challenge? = null

    // Hashtable containing the dictionary of valid words
    private lateinit var wordTable: HashSet<String>

    // property field which returns the word stack in reverse order
    val historyList: List<String>
        get() = wordStack.toList().reversed()

    // the word stack (the rungs of the current word ladder)
    private lateinit var wordStack: Stack<String>

    fun getPreviousWord(): String {
        return if (wordStack.size > 1) {
            wordStack.pop()
        } else wordStack.peek()
    }

    fun peekPreviousWord(): String {
        return wordStack.peek()
    }

    fun wordSubmitted(word: String): Boolean {
        wordStack.push(word)
        return checkWord(word)
    }

    private fun checkWord(word: String): Boolean {
        return word == currentChallenge?.endWord
    }

    fun validLadderWord(word: String): Boolean {
        val prev = peekPreviousWord()
        // must be different from prev
        if (word == prev) return false
        // false if not a real word
        if (!wordTable.contains(word)) return false
        var wordDiff = 0
        for (i in 0 until WORD_SIZE)
            wordDiff += if (word[i] != prev[i]) 1 else 0
        // true if only different by 1 char
        if (wordDiff == 1)
            return true
        // if it is a rearrangement
        if (word.toSet() == prev.toSet())
            return true
        return false
    }

    fun loadHistoryStack(): Stack<String> {
        // TODO this will retrieve stored data from user prefs
        return Stack<String>()
    }

    fun loadWordTable(dictStream: InputStream) {
        wordTable = HashSet()
        dictStream.bufferedReader().forEachLine { wordTable.add(it.uppercase()) }
    }

    fun updateChallenge(challenge: Challenge) {
        val oldChallenge = currentChallenge
        currentChallenge = challenge
        // reset challenge if it is new (new date)
        if (oldChallenge == null || challenge.date != oldChallenge.date)
            initGameData()
    }

    private fun initGameData() {
        wordStack = Stack()
        wordStack.push(currentChallenge?.startWord)
    }

}