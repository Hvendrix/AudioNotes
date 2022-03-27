package com.example.audionotes.core.domain.repository

import com.example.audionotes.core.data.model.AudioNote
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    suspend fun getNotes(): Flow<List<AudioNote>>

    suspend fun getNote(): Flow<AudioNote>

    suspend fun saveNote(audioNote: AudioNote): Long

    suspend fun deleteNotes()
}