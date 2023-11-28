package com.example.wordladder

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wordladder.databinding.FragmentScoreboardBinding
import kotlinx.coroutines.launch

const val REQ_KEY = "SCOREBOARD_REQUEST_KEY"
const val SUB_KEY = "SUBMITTED"

class ScoreboardFragment : Fragment() {

    private val fragArgs : ScoreboardFragmentArgs by navArgs()

    private val viewModel : ScoreboardViewModel by viewModels()

    private var _binding : FragmentScoreboardBinding? = null
    private val binding : FragmentScoreboardBinding
        get() = checkNotNull(_binding) {
        "Binding is null!"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScoreboardBinding.inflate(inflater, container, false)

        // setting the recycler view stuff
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get challenge (should add try-catch here)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.scoreboardFlow.collect { scoreboard ->
                    scoreboard?.let {
                        updateRecycler(scoreboard.nameList, scoreboard.scoreList)
                    }
                }
            }
        }
        setupUI()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_scoreboard, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.share -> {
                sendScore()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun sendScore() {
        val reportIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, R.string.share_subject)
            putExtra(Intent.EXTRA_TEXT,
                String.format(resources.getString(R.string.share_message),
                    fragArgs.startWord, fragArgs.endWord, fragArgs.score)
                )
        }
        val chooserIntent = Intent.createChooser(reportIntent, null)
        startActivity(chooserIntent)
    }

    private fun setupUI() {
        binding.apply {
            challengeHeader.text = String.format(resources.getString(R.string.cha_header), fragArgs.startWord, fragArgs.endWord)
            submitButton.setOnClickListener {
                // share score
                shareScore(fragArgs.score)
            }
            // check if can submit
            if (!fragArgs.canSubmit) {
                binding.submitButton.isEnabled = false
                binding.submitBox.visibility = View.GONE
                setFragmentResult(REQ_KEY, bundleOf(SUB_KEY to true))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateRecycler(names: List<String>, scores: List<Int>) {
        binding.recyclerView.adapter = ScoreboardAdapter(names, scores)
    }

    private fun shareScore(score: Int) {
        val name = binding.submitBox.text.toString()
        binding.submitButton.isEnabled = false
        binding.submitBox.visibility = View.GONE
        val currScoreboard = viewModel.scoreboardFlow.value
        currScoreboard?.let {
            updateRecycler(listOf(name) + it.nameList,
                listOf(score) + it.scoreList)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            ChallengeRepository.get().postScore(name, score)
        }
        setFragmentResult(REQ_KEY, bundleOf(SUB_KEY to true))
    }

}