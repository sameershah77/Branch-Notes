package com.sameer.branchnotes.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sameer.branchnotes.databinding.NoteItemLayoutBinding
import com.sameer.branchnotes.model.Notes
import com.sameer.branchnotes.utils.NotesAdapterItemClick
import com.sameer.branchnotes.utils.NotesDiffUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Collections

class NotesNewAdapter(val clickListener: NotesAdapterItemClick) : ListAdapter<Notes, NotesNewAdapter.ViewHolder>(NotesDiffUtils()) {

    companion object {
        private val colors = listOf(
            Color.parseColor("#86690E0E"),
            Color.parseColor("#860E4569"),
            Color.parseColor("#860E1D69"),
            Color.parseColor("#86310E69"),
            Color.parseColor("#86690E35"),
            Color.parseColor("#860E6917"),
            Color.parseColor("#860E6969"),
            Color.parseColor("#8669520E"),
            Color.parseColor("#8658690E"),
            Color.parseColor("#86460E69")
        )
    }

    inner class ViewHolder(val binding: NoteItemLayoutBinding, val clickListener: NotesAdapterItemClick) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(note: Notes, position: Int) {
            binding.apply {
                title.text = note.title
                desc.text = note.note
                noteCard.setOnClickListener {
                    clickListener.onNoteItemClick(note,position)
                }
                val randomColor = colors[position % colors.size] // Ensures colors are repeated but evenly distributed
                noteCard.setCardBackgroundColor(randomColor)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = NoteItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view,clickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position),position)
    }

    fun removeItem(position: Int) {
        val currentList = currentList.toMutableList() // Get the current list as mutable
        if (position in currentList.indices) {
            currentList.removeAt(position) // Remove the item
            submitList(currentList) // Submit new list, triggers DiffUtil
        }
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        val currentList = currentList.toMutableList() // Convert to mutable list
        if (fromPosition in currentList.indices && toPosition in currentList.indices) {
            Collections.swap(currentList, fromPosition, toPosition) // Swap elements
            submitList(currentList) // Submit new list, triggers DiffUtil
        }
    }
}