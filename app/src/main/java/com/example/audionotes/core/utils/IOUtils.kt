package com.example.audionotes.core.utils

import android.content.Context
import java.io.File

object IOUtils {
    var cacheFolder: File? = null

    fun getCacheDir(context: Context): File? {
        var cache: File? = context.externalCacheDir
        if (cache == null) cache = context.cacheDir
        return cache
    }
}