package com.example.audionotes.core.domain.interactors

import com.example.audionotes.core.domain.repository.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainInteractor(
    private val notesRepository: NotesRepository
){
    suspend fun deleteNotes() = withContext(Dispatchers.IO){
        notesRepository.deleteNotes()
    }
}