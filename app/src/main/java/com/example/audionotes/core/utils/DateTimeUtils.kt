package com.example.audionotes.core.utils

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {

    fun getTimeStamp(timeinMillies: Long): String? {
        var date: String? = null
        val formatter = SimpleDateFormat("dd.MM.yyyy в HH:mm") // modify format
        date = formatter.format(Date(timeinMillies))
        println("Today is $date")
        return date
    }
}