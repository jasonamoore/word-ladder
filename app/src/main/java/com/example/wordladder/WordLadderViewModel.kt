package com.example.wordladder

import androidx.lifecycle.ViewModel
import java.util.Stack

class WordLadderViewModel : ViewModel() {

    val target: String = "test"

    val wordStack: Stack<String>
    var currentWord: String = ""

    init {
        wordStack = Stack<String>()
        // init currentWord
    }

    fun getPreviousWord(): String? {
        return if (wordStack.size > 0) {
            currentWord = wordStack.pop()
            currentWord
        } else null
    }

    fun wordSubmitted(word: String): Boolean {
        pushWord(currentWord)
        currentWord = word
        return checkWord(currentWord)
    }

    fun pushWord(word: String) {
        wordStack.push(word)
    }

    fun checkWord(word: String): Boolean {
        return word.equals(target)
    }

}