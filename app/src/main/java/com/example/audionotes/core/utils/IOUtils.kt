package com.example.audionotes.core.utils

import android.content.Context
import java.io.File

object IOUtils {
    var cacheFolder: File? = null

    /**
     * Return the folder for cache files.
     */
    fun getCacheDir(context: Context): File? {
        var cache: File? = context.getExternalCacheDir()
        if (cache == null) cache = context.getCacheDir()
        return cache
    }
}