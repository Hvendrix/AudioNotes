package com.example.audionotes.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.audionotes.core.data.model.AudioNote

@Database(
    entities = [AudioNote::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun notesDao(): NotesDao
}