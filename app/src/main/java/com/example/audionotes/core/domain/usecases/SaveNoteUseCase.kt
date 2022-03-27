package com.example.audionotes.core.domain.usecases

import com.example.audionotes.core.data.model.AudioNote
import com.example.audionotes.core.domain.repository.NotesRepository

class SaveNoteUseCase(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(audioNote: AudioNote) = notesRepository.saveNote(audioNote)
}