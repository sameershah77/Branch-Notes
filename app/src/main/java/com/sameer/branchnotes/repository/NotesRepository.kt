package com.sameer.branchnotes.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.sameer.branchnotes.db.NotesDB
import com.sameer.branchnotes.model.Notes

class NotesRepository(application: Application) {
    val notesDB = NotesDB.getDatabase(application)
    val dao = notesDB.notesDao()

    suspend fun insertNote(note: Notes) {
        dao.insertNote(note)
    }

    suspend fun updateNote(note: Notes) {
        dao.updateNote(note)
    }

    suspend fun deleteNote(note: Notes) {
        dao.deleteNote(note)
    }

    fun getAllNotes() : LiveData<List<Notes>> {
        return dao.getAllNotes()
    }

    fun getNoteById(noteId: Int): LiveData<Notes> {
        return dao.getNoteById(noteId)
    }

}