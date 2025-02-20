package com.sameer.branchnotes.utils

import com.sameer.branchnotes.model.Notes

interface NotesAdapterItemClick {
    fun onNoteItemClick(note: Notes,position: Int)
}