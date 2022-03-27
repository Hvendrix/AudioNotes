package com.example.audionotes.core.data.repository

import com.example.audionotes.core.data.database.NotesDao
import com.example.audionotes.core.data.model.AudioNote
import kotlinx.coroutines.flow.Flow

class NoteLocalDataSourceImpl(
    val notesDao: NotesDao
) : NoteLocalDataSource {
    override suspend fun getNotes(): Flow<MutableList<AudioNote>> {
        return notesDao.getNotes()
    }

    override suspend fun getNote(id: Long): Flow<AudioNote> {
        return notesDao.getNote(id)
    }

    override suspend fun saveNote(audioNote: AudioNote): Long {
        return notesDao.saveNote(audioNote)

    }

    override suspend fun deleteNotes() {
        return notesDao.deleteNotes()
    }

    override suspend fun updateDuration(id: Long, endDateTime: Long) {
        return notesDao.updateDuration(id, endDateTime)
    }

    override suspend fun updateName(id: Long, name: String) {
        return notesDao.updateName(id, name)
    }
}