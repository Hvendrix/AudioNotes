package com.example.audionotes.core.domain.usecases

import com.example.audionotes.core.domain.repository.NotesRepository

class GetNotesUseCase(
    private val notesRepository: NotesRepository) {
    suspend operator fun invoke() = notesRepository.getNotes()
}