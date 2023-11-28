package com.example.wordladder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wordladder.databinding.ListItemScoreBinding
import com.example.wordladder.databinding.ListItemWordBinding
import com.example.wordladder.databinding.ListItemWordTopBinding

class ScoreboardAdapter(
    private val names: List<String>,
    private val scores: List<Int>) : RecyclerView.Adapter<ScoreHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemScoreBinding.inflate(inflater, parent, false)
        return ScoreHolder(binding)
    }

    override fun onBindViewHolder(holder: ScoreHolder, position: Int) {
        holder.bind(names[position], scores[position])
    }

    override fun getItemCount(): Int {
        return names.size
    }

}

class ScoreHolder(private val binding: ListItemScoreBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(name: String, score: Int) {
        binding.name.text = name
        binding.score.text = score.toString()
    }
}