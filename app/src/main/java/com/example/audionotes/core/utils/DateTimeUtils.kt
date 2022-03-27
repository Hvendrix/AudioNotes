package com.example.audionotes.core.utils

import timber.log.Timber
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {

    fun getTimeStamp(timeinMillies: Long): String? {
        val todayFormatterTest = SimpleDateFormat("dd.MM.yyyy")
        return if(todayFormatterTest.format(timeinMillies)==todayFormatterTest.format(System.currentTimeMillis())){
            val todayFormatter = SimpleDateFormat("Сегодня в HH:mm")
            todayFormatter.format(Date(timeinMillies))
        } else{
            val formatter = SimpleDateFormat("dd.MM.yyyy в HH:mm") // modify format
            formatter.format(Date(timeinMillies))
        }
    }

    fun getDuration(timeinMillies: Long): String?{
        var date: String? = null
        val formatter = SimpleDateFormat("mm:ss") // modify format
        date = formatter.format(Date(timeinMillies))
        return date
    }
    fun getOperator(): String? {
        return "-"
    }

//    fun getResult(firstDate: LocalDate?, secondDate: LocalDate?) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val period: Period =  Period.between(secondDate, firstDate)
//            println(period.getYears().toString() + "." + period.getMonths() + "." + period.getDays() + "" + period.min)
//        } else {
//            TODO("VERSION.SDK_INT < O")
//        }
//
//    }


    fun getDuration(date1: Date, date2: Date): String?{
        println("Первая дата: $date1")
        println("Вторая дата: $date2")
        var result = StringBuilder()


        val milliseconds = date2.time - date1.time
        Timber.v("\nРазница между датами в миллисекундах: $milliseconds")

        // 24 часа = 1 440 минут = 1 день
        val days = (milliseconds / (24 * 60 * 60 * 1000)).toInt()
        Timber.v("Разница между датами в днях: $days")


        // 3 600 секунд = 60 минут = 1 час
        val hours = ((milliseconds / (60 * 60 * 1000)%24)).toInt()
        Timber.v("Разница между датами в часах: $hours")

        // 60 000 миллисекунд = 60 секунд = 1 минута
        val minutes = ((milliseconds / (60 * 1000)) % 60).toInt()
        Timber.v("Разница между датами в минутах: $minutes")
        result.append("${Math.abs(minutes)}:")

        val secondZero = ""

        // 1000 миллисекунд = 1 секунда
        val seconds = ((milliseconds / 1000) % 60).toInt()
        Timber.v("Разница между датами в секундах: $seconds")
        result.append("${if(Math.abs(seconds)>9) "" else "0"}${Math.abs(seconds)}")




        return result.toString()
    }
}