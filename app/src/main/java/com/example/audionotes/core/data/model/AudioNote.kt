package com.example.audionotes.core.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notes")
data class AudioNote(
    val name: String,
    val startDateTime: Long,
    val endDateTime: Int,
    @ColumnInfo(name = "note_path")
    val notePath: String,
    @PrimaryKey(autoGenerate = true)
    val id:Int= 0,
    )
