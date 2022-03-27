package com.example.audionotes.core.data.repository

import com.example.audionotes.core.data.model.AudioNote
import kotlinx.coroutines.flow.Flow

interface NoteLocalDataSource {
    suspend fun getNotes(): Flow<MutableList<AudioNote>>

    suspend fun getNote(id: Long): Flow<AudioNote>

    suspend fun saveNote(audioNote: AudioNote): Long

    suspend fun deleteNotes()

    suspend fun updateDuration(id: Long, endDateTime: Long)
}