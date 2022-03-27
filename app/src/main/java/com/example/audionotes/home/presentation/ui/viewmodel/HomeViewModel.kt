package com.example.audionotes.home.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.audionotes.core.data.model.AudioNote
import com.example.audionotes.home.domain.interactors.HomeInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {


    @Inject
    lateinit var homeInteractor: HomeInteractor

    private val _notesList = MutableStateFlow<MutableList<AudioNote>>(
        mutableListOf()
    )
    val notesList: StateFlow<MutableList<AudioNote>> = _notesList.asStateFlow()


    private val _playedList = MutableStateFlow<List<Boolean>>(
        mutableListOf()
    )
    val playedList: StateFlow<List<Boolean>> = _playedList.asStateFlow()


    init {

    }


    suspend fun saveAudioNote(audioNote: AudioNote) : Long{
             return homeInteractor.saveNote(audioNote)
//            Timber.v("t5 x is " + x)
//            getAudioNotes()
    }

    suspend fun getAudioNotes() {
        viewModelScope.launch {
            homeInteractor.getNotes().flowOn(Dispatchers.IO).collect {
                if(!it.isNullOrEmpty()) {
                    _notesList.value = it
                    Timber.v("t5 list" + it.size + " " + it.get(0).startDateTime)
                }
            }
        }
    }


    fun updateDuration(id: Long, endDateTime: Long){
        viewModelScope.launch {
            homeInteractor.updateDuration(id, endDateTime)
            getAudioNotes()
        }
    }


//    suspend fun getNote(id: Long): AudioNote{
//           return homeInteractor.getNote(id).
//    }



}