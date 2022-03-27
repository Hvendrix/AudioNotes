package com.example.audionotes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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