package com.example.wordladder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wordladder.databinding.FragmentWordLadderBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class WordLadderFragment : Fragment() {

    private val viewModel : WordLadderViewModel by viewModels()

    private var _binding : FragmentWordLadderBinding? = null
    private val binding : FragmentWordLadderBinding
        get() = checkNotNull(_binding) {
        "Binding is null!"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        // set listener for scoreboard
        setFragmentResultListener(REQ_KEY) { key, bundle ->
            val submitted = bundle.getBoolean(SUB_KEY)
            viewModel.submitted = submitted
        }
        // load up stuff
        viewModel.loadWordTable(
            resources.openRawResource(R.raw.dictionary))
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWordLadderBinding.inflate(inflater, container, false)

        // setting the recycler view stuff
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get challenge (should add try-catch here)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.challengeFlow.collect { challenge ->
                    challenge?.let {
                        viewModel.updateChallenge(it)
                        updateRecycler(viewModel.historyList)
                        // "turn on" the game (enable buttons)
                        if (!viewModel.won)
                            enableUI()
                    }
                }
            }
        }
        setupUI()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_wordladder, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.help -> {
                findNavController().navigate(WordLadderFragmentDirections.showHelp())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
                        root, R.string.invalid,
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                val result = viewModel.wordSubmitted(inputText)
                if (result) {
                    Snackbar.make(
                        root, R.string.finished,
                        Snackbar.LENGTH_SHORT
                    ).show()
                    viewModel.won = true
                    binding.scoreCount.text = String.format(getString(R.string.score_count), viewModel.score)
                    enableScoreboard()
                } else {
                    updateRecycler(viewModel.historyList)
                    editText.setText("") // clear input
                }
            }
            // get previous word from view model
            prevWord.setOnClickListener {
                val prescore = viewModel.score
                viewModel.getPreviousWord()
                if (prescore == 1) {
                    Snackbar.make(
                        root, R.string.no_previous,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                updateRecycler(viewModel.historyList)
            }
            // clear the word
            clearWord.setOnClickListener {
                editText.setText("") // clear
            }
            playAgain.setOnClickListener {
                viewModel.initGameData()
                enableUI()
                updateRecycler(viewModel.historyList)
            }
            showScores.setOnClickListener {
                val cur = viewModel.currentChallenge
                cur?.let {
                    findNavController().navigate(
                        WordLadderFragmentDirections.toScoreboard(
                            viewModel.score,
                            it.startWord,
                            it.endWord,
                            !viewModel.submitted
                        )
                    )
                }
            }
        }
        if (viewModel.won)
            enableScoreboard()
    }
    private fun enableScoreboard() {
        binding.apply {
            prevWord.visibility = View.GONE
            clearWord.visibility = View.GONE
            showScores.visibility = View.VISIBLE
            playAgain.visibility = View.VISIBLE
            editText.isEnabled = false
        }
    }

    private fun enableUI() {
        binding.apply {
            prevWord.isEnabled = true
            submitWord.isEnabled = true
            clearWord.visibility = View.VISIBLE
            prevWord.visibility = View.VISIBLE
            showScores.visibility = View.GONE
            playAgain.visibility = View.GONE

            editText.setText("")
            editText.isEnabled = true

            // display target word
            targetWord.text = viewModel.currentChallenge?.endWord
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateRecycler(list: List<String>) {
        binding.scoreCount.text = String.format(getString(R.string.score_count), viewModel.score)
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