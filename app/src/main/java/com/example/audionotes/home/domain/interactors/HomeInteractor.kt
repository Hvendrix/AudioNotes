package com.example.audionotes.home.domain.interactors

import com.example.audionotes.core.data.model.AudioNote
import com.example.audionotes.core.domain.repository.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber

class HomeInteractor(
    private val notesRepository: NotesRepository
){
//    suspend fun getNotes(): ServerResponseMusicTest = withContext(Dispatchers.IO) {
//        return@withContext notesRepository.getNotes()
//            .execute().body() ?: error("not found")
//    }

    suspend fun getNotes(): Flow<List<AudioNote>> = withContext(Dispatchers.IO) {
        return@withContext notesRepository.getNotes()
    }

    suspend fun saveNote(audioNote: AudioNote) = withContext(Dispatchers.IO){
        return@withContext notesRepository.saveNote(audioNote)
    }


}