package com.example.audionotes.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteEntity(
    @PrimaryKey
    val id:Int,
    val name: String,
    val date: Int,
    val duration: Int
    )
