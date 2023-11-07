package com.example.wordladder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wordladder.databinding.FragmentWordLadderBinding
import com.google.android.material.snackbar.Snackbar

class WordLadderFragment : Fragment() {

    private val viewModel : WordLadderViewModel by viewModels()

    private var _binding : FragmentWordLadderBinding? = null
    private val binding : FragmentWordLadderBinding
        get() = checkNotNull(_binding) {
        "Binding is null!"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // load up stuff
        viewModel.loadWordTable(
            resources.openRawResource(R.raw.dictionary))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWordLadderBinding.inflate(inflater, container, false)

        // setting the recycler view stuff
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        updateRecycler(viewModel.historyList)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.apply {
            // on text change we should force uppercase
            editText.doOnTextChanged { etext, _, _, _ ->
                val text = etext.toString()
                // break string on cursor position:
                val cursorPos = editText.selectionStart
                val sanitizeLeft = sanitize(text.substring(0, cursorPos))
                val sanitizeAll = sanitize(text)
                if (text != sanitizeAll) {
                    editText.setText(sanitizeAll)
                    // smart way to move cursor (length of sanitized left split)
                    editText.setSelection(sanitizeLeft.length)
                }
            }
            // submit a word to the view model
            submitWord.setOnClickListener {
                val inputText = editText.text.toString().uppercase()
                if (!viewModel.validLadderWord(inputText)) {
                    Snackbar.make(
                        root, "Bad input!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                val result = viewModel.wordSubmitted(inputText)
                if (result) {
                    Snackbar.make(
                        root, R.string.success,
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    updateRecycler(viewModel.historyList)
                    editText.setText("") // clear input
                }
            }
            // get previous word from view model
            prevWord.setOnClickListener {
                val prev = viewModel.getPreviousWord()
                prev?.let {
                    editText.setText(it, TextView.BufferType.EDITABLE)
                    updateRecycler(viewModel.historyList)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun updateRecycler(list: List<String>) {
        binding.recyclerView.adapter = LadderAdapter(list)
    }

    private fun sanitize(text: String): String {
        val sanitized = StringBuilder()
        text.forEach {
            val u = it.uppercaseChar()
            if (u in 'A'..'Z')
                sanitized.append(u)
        }
        return sanitized.toString()
    }

}