package com.example.wordladder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wordladder.databinding.ListItemWordBinding
import com.example.wordladder.databinding.ListItemWordTopBinding

const val TOP_RUNG = 0
const val SMALL_RUNG = 1

class LadderAdapter(private val words: List<String>) : RecyclerView.Adapter<LadderHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LadderHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TOP_RUNG) {
            val binding = ListItemWordTopBinding.inflate(inflater, parent, false)
            TopRung(binding)
        }
        else {
            val binding = ListItemWordBinding.inflate(inflater, parent, false)
            SmallRung(binding)
        }
    }

    override fun onBindViewHolder(holder: LadderHolder, position: Int) {
        holder.bind(words[position])
    }

    override fun getItemCount(): Int {
        return words.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == TOP_RUNG) TOP_RUNG else SMALL_RUNG
    }
}

abstract class LadderHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {
    abstract fun bind(text : String)
}
class SmallRung(val binding: ListItemWordBinding) : LadderHolder(binding.root) {
    override fun bind(text : String) {
        binding.word.text = text
    }
}
class TopRung(val binding: ListItemWordTopBinding) : LadderHolder(binding.root) {
    override fun bind(text : String) {
        binding.word.text = text
    }
}