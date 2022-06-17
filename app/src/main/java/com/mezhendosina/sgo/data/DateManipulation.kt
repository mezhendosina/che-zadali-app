package com.mezhendosina.sgo.data

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class DateManipulation(val date: String) {
    private val locale = Locale("ru", "RU")
    private val a = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

    fun dateToRussian(uppercase: Boolean): String {
        val a = a.parse(date)
        return when (uppercase) {
            true -> SimpleDateFormat("EEEE, dd MMMM", locale).format(a!!)
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            false -> SimpleDateFormat("Сегодня EE, dd MMMM", locale).format(a!!)
        }
    }

    fun dateToRussianWithTime(): String {
        val a = a.parse(date)
        return SimpleDateFormat("dd.MM.yyyy hh:mm").format(a!!)
    }

    //Format date
    fun dateFormatter(): String {
        val a = a.parse(date)
        return SimpleDateFormat("dd.MM.yyyy", locale).format(a!!)
    }

    fun dateToTime(): Long {
        val a = a.parse(date)
        return a!!.time
    }

    fun messageDate(): String {
        val a = SimpleDateFormat("yyyy-MM-dd'T'00:00:00").parse(date)
        return SimpleDateFormat("dd MMMM", locale).format(a!!)
    }
}

@SuppressLint("SimpleDateFormat")
fun weekStart(week: Int): String {
    val s = SimpleDateFormat("w.yyyy").format(Date().time)
    val a = SimpleDateFormat("w.yyyy").parse(s)!!.time + week * 7 * 24 * 60 * 60 * 1000
    return SimpleDateFormat("yyyy-MM-dd").format(a)
}

@SuppressLint("SimpleDateFormat")
fun weekEnd(week: Int): String {
    val s = SimpleDateFormat("w.yyyy").format(Date().time)
    val a =
        SimpleDateFormat("w.yyyy").parse(s)!!.time + week * 7 * 24 * 60 * 60 * 1000 + 6 * 24 * 60 * 60 * 1000
    return SimpleDateFormat("yyyy-MM-dd").format(a)
}
