package com.example.audionotes.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.audionotes.core.data.model.NoteEntity

@Database(
    entities = [NoteEntity::class],
    version = 1,
    exportSchema = false
)
//@TypeConverters(ResultConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun notesDao(): NotesDao
}