package com.example.audionotes.core.data.repository

import com.example.audionotes.core.data.database.NotesDao
import com.example.audionotes.core.data.model.AudioNote
import kotlinx.coroutines.flow.Flow

class NoteLocalDataSourceImpl(
    val notesDao: NotesDao
): NoteLocalDataSource {
    override suspend fun getNotes(): Flow<List<AudioNote>> {
        return notesDao.getNotes()
    }

    override suspend fun saveNote(audioNote: AudioNote): Long {
        return notesDao.saveNote(audioNote)

    }

    override suspend fun deleteNotes() {
        return notesDao.deleteNotes()
    }
}