package com.example.wordladder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
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

        // test code
        binding.submitWord.setOnClickListener {
            val inputText = binding.editText.getText().toString()
            val result = viewModel.wordSubmitted(inputText)
            if (result)
                Snackbar.make(binding.root, R.string.success,
                    Snackbar.LENGTH_SHORT).show()
        }

        binding.prevWord.setOnClickListener {
            val prev = viewModel.getPreviousWord()
            prev?.let {
                binding.editText.setText(it, TextView.BufferType.EDITABLE)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun updateRecycler(list: List<String>) {
        binding.recyclerView.adapter = LadderAdapter(list)
    }

}