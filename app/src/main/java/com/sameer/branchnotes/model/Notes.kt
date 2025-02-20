package com.sameer.branchnotes.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Notes(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val note: String,
    val time: String
)
