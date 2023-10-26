package com.example.wordladder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import com.example.wordladder.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: WordLadderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root)

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
}