package com.example.audionotes.home.presentation.Controllers

import android.media.MediaRecorder
import com.example.audionotes.core.utils.IOUtils
import timber.log.Timber
import java.io.File


// взято отсюда https://github.com/otopba/record_view
// Только сменил директорию и немного топорно, но закешировал путь в отдельном классе, чтобы не было утечек памяти
class RecordController() {

    private var audioRecorder: MediaRecorder? = null



    fun start(fileName: String):String {
        Timber.v("Start")
        var path = ""
        audioRecorder = MediaRecorder().apply {
            path = getAudioPath(fileName)
            Timber.v("t5 path " + path)
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(path)
            prepare()
            start()
            Timber.v("t5 start" + path)
        }
        return path
    }

    private fun getAudioPath(fileName: String): String {
        val myExternalFile = File(IOUtils.cacheFolder, fileName)
        return "$myExternalFile"
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