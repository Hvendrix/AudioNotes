package com.example.audionotes.core.data.repository

import com.example.audionotes.core.data.model.AudioNote
import kotlinx.coroutines.flow.Flow

interface NoteLocalDataSource {
    suspend fun getNotes(): Flow<List<AudioNote>>

    suspend fun saveNote(audioNote: AudioNote): Long

    suspend fun deleteNotes()
}