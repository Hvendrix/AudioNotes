package com.example.audionotes.core.data.database

import androidx.room.*
import com.example.audionotes.core.data.model.AudioNote
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {
//    onConflict = OnConflictStrategy.REPLACE
    @Insert()
    fun saveNote(audioNote: AudioNote): Long


    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNote(id: Long): Flow<AudioNote>

    @Query("SELECT * FROM notes")
    fun getNotes(): Flow<List<AudioNote>>

    @Query("DELETE FROM notes")
    fun deleteNotes()
}