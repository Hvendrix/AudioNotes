package com.example.audionotes

import android.app.Application
import com.example.audionotes.core.utils.IOUtils
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
//        ExecutorBuildType.execute()
        super.onCreate()
        IOUtils.cacheFolder = IOUtils.getCacheDir(this)
        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }
}