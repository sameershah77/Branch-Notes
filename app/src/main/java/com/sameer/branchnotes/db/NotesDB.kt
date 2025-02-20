package com.sameer.branchnotes.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sameer.branchnotes.model.Notes
import com.sameer.branchnotes.utils.NotesDao
import android.content.Context
import androidx.room.Room

@Database(entities = [Notes::class], version = 1)
abstract class NotesDB : RoomDatabase() {
    abstract fun notesDao(): NotesDao

    companion object {
        @Volatile
        private var INSTANCE: NotesDB? = null

        // Singleton pattern for database instance
        fun getDatabase(context: Context): NotesDB {
            // Return existing instance if available
            return INSTANCE ?: synchronized(this) {
                // If no instance, create a new one
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDB::class.java,
                    "notes_database" // Name of your database file
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
