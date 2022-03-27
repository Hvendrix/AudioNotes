package com.example.audionotes.core.domain.repository

import com.example.audionotes.core.data.model.AudioNote
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    suspend fun getNotes(): Flow<MutableList<AudioNote>>

    suspend fun getNote(id: Long): Flow<AudioNote>

    suspend fun saveNote(audioNote: AudioNote): Long

    suspend fun deleteNotes()

    suspend fun updateDuration(id: Long, endDateTime: Long)

    suspend fun updateName(id: Long, name: String)
}