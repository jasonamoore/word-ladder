package com.example.wordladder

import androidx.lifecycle.ViewModel
import java.util.Stack

class WordLadderViewModel : ViewModel() {

    val target: String = "test"

    val historyList: List<String>
        get() = wordStack.toList()

    private val wordStack: Stack<String> = loadHistoryStack()
    private var currentWord: String = if (!wordStack.isEmpty()) wordStack.peek() else ""
        get() = currentWord


    private fun loadHistoryStack(): Stack<String> {
        // this will retrieve stored data from user prefs
        return Stack<String>()
    }

    fun getPreviousWord(): String? {
        return if (!wordStack.isEmpty()) {
            currentWord = wordStack.pop()
            currentWord
        } else null
    }

    fun wordSubmitted(word: String): Boolean {
        pushWord(currentWord)
        currentWord = word
        return checkWord(currentWord)
    }

    private fun pushWord(word: String) {
        wordStack.push(word)
    }

    private fun checkWord(word: String): Boolean {
        return word == target
    }

    private fun updateRecycler() {
        // this should pass the new stack data to the recycler
        // and call updateUI on fragment
    }

}