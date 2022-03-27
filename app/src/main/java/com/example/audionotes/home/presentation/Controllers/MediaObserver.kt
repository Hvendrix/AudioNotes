package com.example.audionotes.home.presentation.Controllers

import java.util.concurrent.atomic.AtomicBoolean

//private val observer: MediaObserver? = null
//
//private class MediaObserver : Runnable {
//    private val stop: AtomicBoolean = AtomicBoolean(false)
//    fun stop() {
//        stop.set(true)
//    }
//
//    override fun run() {
//        while (!stop.get()) {
//            progress_bar.setProgress((mediaPlayer.getCurrentPosition() as Double / mediaPlayer.getDuration() as Double * 100).toInt())
//            try {
//                Thread.sleep(200)
//            } catch (ex: Exception) {
////                Logger.log(this@ToolSoundActivity, ex)
//            }
//        }
//    }
//}