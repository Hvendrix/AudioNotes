package com.example.audionotes.core.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val name: String,
    val date: Int,
    val duration: Int,
    @ColumnInfo(name = "note_path")
    val notePath: String
    )
