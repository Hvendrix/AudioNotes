package com.example.audionotes.core.utils

import timber.log.Timber
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {

    fun getTimeStamp(timeinMillies: Long): String? {
        val todayFormatterTest = SimpleDateFormat("dd.MM.yyyy")
        return if (todayFormatterTest.format(timeinMillies) == todayFormatterTest.format(System.currentTimeMillis())) {
            val todayFormatter = SimpleDateFormat("Сегодня в HH:mm")
            todayFormatter.format(Date(timeinMillies))
        } else {
            val formatter = SimpleDateFormat("dd.MM.yyyy в HH:mm") // modify format
            formatter.format(Date(timeinMillies))
        }
    }

    fun getDurationMilliseconds(date1: Date, date2: Date): Long {
        return date2.time - date1.time
    }

    fun getElapsedTime(timeinMillies: Long): String {
        val formatter = SimpleDateFormat("mm:ss / ") // modify format
        return formatter.format(Date(timeinMillies))
    }

    fun getDuration(date1: Date, date2: Date): String {
        var result = StringBuilder()

        val milliseconds = getDurationMilliseconds(date1, date2)
        Timber.v("\nРазница между датами в миллисекундах: $milliseconds")

        val hours = ((milliseconds / (60 * 60 * 1000) % 24)).toInt()
        Timber.v("Разница между датами в часах: $hours")
        if (hours != 0) result.append("${Math.abs(hours)}:")


        val minutes = ((milliseconds / (60 * 1000)) % 60).toInt()
        Timber.v("Разница между датами в минутах: $minutes")
        result.append("${Math.abs(minutes)}:")

        val seconds = ((milliseconds / 1000) % 60).toInt()
        Timber.v("Разница между датами в секундах: $seconds")
        result.append("${if (Math.abs(seconds) > 9) "" else "0"}${Math.abs(seconds)}")

        return result.toString()
    }
}