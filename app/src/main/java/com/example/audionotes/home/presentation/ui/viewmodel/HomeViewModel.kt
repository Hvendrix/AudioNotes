package com.example.audionotes.home.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.audionotes.core.data.model.NoteEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel(){

    private val _notesList = MutableStateFlow<List<NoteEntity>>(
        mutableListOf(NoteEntity(1,"2", 3, 4, ""))
    )
    val notesList: StateFlow<List<NoteEntity>> = _notesList.asStateFlow()

    init {
        _notesList.value =mutableListOf(
            NoteEntity(1,"1", 3, 4, ""),
            NoteEntity(2,"2", 3, 4, ""),
            NoteEntity(3,"3", 3, 4, "")
        )
    }


}