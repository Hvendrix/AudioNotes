package com.example.audionotes.home.presentation.Controllers

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.CountDownTimer
import com.example.audionotes.core.data.model.AudioNote
import com.example.audionotes.core.utils.DateTimeUtils
import com.example.audionotes.core.utils.IOUtils
import com.example.audionotes.home.presentation.ui.list.adapter.AdapterNotes
import timber.log.Timber
import java.io.File
import java.util.*

class PlayController() {

    private var mediaPlayer: MediaPlayer? = null
    private var timer: CountDownTimer? = null

    fun playNote(item: AudioNote, adapterNotes: AdapterNotes) {
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            var myExternalFile = File(IOUtils.cacheFolder, item.notePath)
            setDataSource(myExternalFile.absolutePath)
            prepare()
            start()
        }
//        mediaPlayer?.setOnCompletionListener {
//            adapterNotes.updatePlaying(item, false)
//        }

        val durationTime = Math.abs(DateTimeUtils.getDurationMilliseconds(Date(item.endDateTime), Date(item.startDateTime)))
        Timber.v("t5 duration" + durationTime)
        timer = object: CountDownTimer(durationTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                adapterNotes.updatePlaying(item, true, durationTime-millisUntilFinished)
                Timber.v("timer "+ (durationTime-millisUntilFinished))
            }

            override fun onFinish() {
                adapterNotes.updatePlaying(item, false)
            }
        }
        timer?.start()

        mediaPlayer?.start()
    }

    fun stopPlayNote() {
        mediaPlayer?.stop()
        timer?.cancel()
    }


    fun stillPlaying():Boolean {
        return mediaPlayer?.isPlaying==true
    }


}