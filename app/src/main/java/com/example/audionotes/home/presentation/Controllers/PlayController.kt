package com.example.audionotes.home.presentation.Controllers

import android.media.AudioAttributes
import android.media.MediaPlayer
import com.example.audionotes.core.utils.IOUtils
import timber.log.Timber
import java.io.File

class PlayController() {

    private var mediaPlayer: MediaPlayer? = null

            fun playNote(fileName: String){
                mediaPlayer = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    var myExternalFile = File(IOUtils.cacheFolder,fileName)
                    Timber.v("t5 inner path " + myExternalFile)
                    setDataSource(myExternalFile.absolutePath)
                    prepare()
                    start()
                }
                mediaPlayer?.start()
            }


}