package com.example.audionotes.core.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.audionotes.core.data.model.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveNote(note: NoteEntity): Long


    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNote(id: Long): Flow<NoteEntity>

    @Query("SELECT * FROM notes")
    fun getNotes(): Flow<List<NoteEntity>>
}