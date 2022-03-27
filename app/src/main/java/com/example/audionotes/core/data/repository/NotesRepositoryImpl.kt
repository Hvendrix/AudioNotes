package com.example.audionotes.core.data.repository

import com.example.audionotes.core.data.model.AudioNote
import com.example.audionotes.core.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow

class NotesRepositoryImpl(
    private val localDataSource: NoteLocalDataSource
) : NotesRepository {
    override suspend fun getNotes(): Flow<MutableList<AudioNote>> {
        return localDataSource.getNotes()
    }

    override suspend fun getNote(id: Long): Flow<AudioNote> {
        return localDataSource.getNote(id)
    }

    override suspend fun saveNote(audioNote: AudioNote): Long {
        return localDataSource.saveNote(audioNote)
    }

    override suspend fun deleteNotes() {
        return localDataSource.deleteNotes()
    }

    override suspend fun updateDuration(id: Long, endDateTime: Long) {
        return localDataSource.updateDuration(id, endDateTime)
    }

    override suspend fun updateName(id: Long, name: String) {
        return localDataSource.updateName(id, name)
    }
}