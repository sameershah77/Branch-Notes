package com.sameer.branchnotes.utils

import androidx.recyclerview.widget.DiffUtil
import com.sameer.branchnotes.model.Notes

class NotesDiffUtils : DiffUtil.ItemCallback<Notes>() {
    override fun areItemsTheSame(oldItem: Notes, newItem: Notes): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Notes, newItem: Notes): Boolean {
        return oldItem == newItem
    }
}