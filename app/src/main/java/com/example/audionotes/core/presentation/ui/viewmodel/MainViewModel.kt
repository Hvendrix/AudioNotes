package com.example.audionotes.core.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.audionotes.core.domain.interactors.MainInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    @Inject
    lateinit var mainInteractor: MainInteractor

    fun deleteNotes(){
        viewModelScope.launch {
            mainInteractor.deleteNotes()
        }

    }
}