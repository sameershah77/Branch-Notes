package com.sameer.branchnotes.utils

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.sameer.branchnotes.model.Notes

@Dao
interface NotesDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertNote(note: Notes)

    @Update
    suspend fun updateNote(note: Notes)

    @Delete
    suspend fun deleteNote(note: Notes)

    @Query("SELECT * FROM Notes")
    fun getAllNotes(): LiveData<List<Notes>>

    @Query("SELECT * FROM Notes WHERE id = :noteId")
    fun getNoteById(noteId: Int): LiveData<Notes>

}