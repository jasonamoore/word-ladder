package com.example.wordladder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wordladder.databinding.ListItemWordBinding

class LadderAdapter(private val words: List<String>) : RecyclerView.Adapter<LadderHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LadderHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemWordBinding.inflate(inflater, parent, false)
        return LadderHolder(binding)
    }

    override fun onBindViewHolder(holder: LadderHolder, position: Int) {
        holder.binding.word.text = words[position]
    }

    override fun getItemCount(): Int {
        return words.size
    }
}

class LadderHolder(val binding: ListItemWordBinding) : RecyclerView.ViewHolder(binding.root) {
}