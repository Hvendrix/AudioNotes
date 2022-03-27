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

    private val _notesList = MutableStateFlow<List<AudioNote>>(
        mutableListOf(AudioNote( "2", 3, 4, ""))
    )
    val notesList: StateFlow<List<AudioNote>> = _notesList.asStateFlow()

    init {
//        viewModelScope.launch {
//            getAudioNotes()
//        }

//        _notesList.value = mutableListOf(
//            AudioNote(1, "1", 3, 4, ""),
//            AudioNote(2, "2", 3, 4, ""),
//            AudioNote(3, "3", 3, 4, "")
//        )
    }


    fun saveAudioNote(audioNote: AudioNote) {
        viewModelScope.launch {
            val x = homeInteractor.saveNote(audioNote)
            Timber.v("t5 x is " + x)
            getAudioNotes()
//            bookmarkBookUseCase.invoke(mapper.fromBookWithStatusToVolume(book))
        }
    }

    suspend fun getAudioNotes() {
        viewModelScope.launch {
            homeInteractor.getNotes().flowOn(Dispatchers.IO).collect {
                _notesList.value = it
                Timber.v("t5 list" + it.size + " " + it.get(0).date)
            }
//            bookmarkBookUseCase.invoke(mapper.fromBookWithStatusToVolume(book))
        }
    }


//    fun playNote(path: String) {
//        var myExternalFile: File?=null
//        val filename = "name123"
//        if (filename.toString() != null && filename.toString().trim() != "") {
//
//            val mediaPlayer = MediaPlayer().apply {
//                setAudioAttributes(
//                    AudioAttributes.Builder()
//                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                        .setUsage(AudioAttributes.USAGE_MEDIA)
//                        .build()
//                )
//                myExternalFile = File(getAppl.getExternalFilesDir(filepath), filename)
//                setDataSource(myExternalFile!!.absolutePath)
//                prepare()
//                start()
//            }
//            mediaPlayer.start()
//        }
//    }


}