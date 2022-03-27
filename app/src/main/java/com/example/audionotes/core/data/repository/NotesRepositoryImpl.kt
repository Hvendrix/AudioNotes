package com.example.audionotes.core.data.repository

import com.example.audionotes.core.data.model.AudioNote
import com.example.audionotes.core.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow

class NotesRepositoryImpl(
private val localDataSource: NoteLocalDataSource
) : NotesRepository {
    override suspend fun getNotes(): Flow<List<AudioNote>> {
        return localDataSource.getNotes()
    }

    override suspend fun getNote(): Flow<AudioNote> {
        TODO("Not yet implemented")
    }

    override suspend fun saveNote(audioNote: AudioNote): Long {
        return localDataSource.saveNote(audioNote)
    }

    override suspend fun deleteNotes() {
        return localDataSource.deleteNotes()
    }
}