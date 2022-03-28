package com.example.audionotes

import android.app.Application
import android.os.StrictMode
import com.example.audionotes.core.utils.IOUtils
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        IOUtils.cacheFolder = IOUtils.getCacheDir(this)
        if(BuildConfig.DEBUG){
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork() // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build()
            )
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build()
            )
            Timber.plant(Timber.DebugTree())
        }

        VK.addTokenExpiredHandler(tokenTracker)
    }


    private val tokenTracker = object: VKTokenExpiredHandler {
        override fun onTokenExpired() {
            Timber.v("token expired")
        }
    }
}