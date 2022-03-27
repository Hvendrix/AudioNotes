package com.example.audionotes.home.presentation.Controllers

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import com.example.audionotes.core.data.model.AudioNote
import com.example.audionotes.core.utils.IOUtils
import com.example.audionotes.home.presentation.ui.list.adapter.AdapterNotes
import timber.log.Timber
import java.io.File

class PlayController() {

    private var mediaPlayer: MediaPlayer? = null

    fun playNote(item: AudioNote, adapterNotes: AdapterNotes) {
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            var myExternalFile = File(IOUtils.cacheFolder, item.notePath)
            Timber.v("t5 inner path " + myExternalFile)
            setDataSource(myExternalFile.absolutePath)
            prepare()
            start()
        }
        mediaPlayer?.setOnCompletionListener {
            Timber.v("t5 completed")
            adapterNotes.updatePlaying(item, false)
//            if(previous!=null) {
//                playController.stopPlayNote()
//                adapter.updatePlaying(previous!!, false)
//                previous = null
//            }
        }
        mediaPlayer?.start()
    }

    fun stopPlayNote() {
        mediaPlayer?.stop()
    }


    fun stillPlaying():Boolean {
        return mediaPlayer?.isPlaying==true
    }


}