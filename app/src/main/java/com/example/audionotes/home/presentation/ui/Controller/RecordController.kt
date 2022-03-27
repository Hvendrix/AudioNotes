package com.example.audionotes.home.presentation.ui.Controller

import android.content.Context
import android.media.MediaRecorder
import android.util.Log
import timber.log.Timber
import java.io.File
import java.lang.Exception


// взято отсюда https://github.com/otopba/record_view
class RecordController(private val context: Context) {

    private var audioRecorder: MediaRecorder? = null



    fun start() {
        Timber.v("Start")
        audioRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(getAudioPath())
            prepare()
            start()
        }
    }

    private fun getAudioPath(): String {
        val filepath = "MyFileStorage"
//        return "${context.cacheDir.absolutePath}${File.pathSeparator}${System.currentTimeMillis()}.wav"
        var filename = "name123"
        val myExternalFile = File(context.getExternalFilesDir(filepath),filename)
        return "$myExternalFile"
//        return "${context.cacheDir.absolutePath}${File.pathSeparator}.wav"
    }

    fun stop() {
        audioRecorder?.let {
            Timber.v("Stop")
            it.stop()
            it.release()
        }
        audioRecorder = null
    }

    fun isAudioRecording() = audioRecorder != null

    fun getVolume() = audioRecorder?.maxAmplitude ?: 0

}