package com.example.wordladder

import androidx.lifecycle.ViewModel
import java.io.File
import java.io.InputStream
import java.util.Stack
import kotlin.math.abs

const val WORD_SIZE = 4

class WordLadderViewModel : ViewModel() {

    private val target: String = "test"

    val historyList: List<String>
        get() = wordStack.toList().reversed()

    private lateinit var wordTable: HashSet<String>
    private var wordStack: Stack<String> = Stack()
    //private lateinit var wordStack: Stack<String>
    //private lateinit var currentWord: String
    private var currentWord: String = ""

    // default word
    init {
        wordStack.push("WORD")
    }

    fun getPreviousWord(): String {
        return if (wordStack.size > 1) {
            currentWord = wordStack.pop()
            currentWord
        } else wordStack.peek()
    }

    fun peekPreviousWord(): String {
        return wordStack.peek()
    }

    fun wordSubmitted(word: String): Boolean {
        wordStack.push(word)
        currentWord = word
        return checkWord(currentWord)
    }

    private fun checkWord(word: String): Boolean {
        return word == target
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
        // this will retrieve stored data from user prefs
        return Stack<String>()
    }

    fun loadWordTable(dictStream: InputStream) {
        wordTable = HashSet()
        dictStream.bufferedReader().forEachLine { wordTable.add(it.uppercase()) }
    }

}