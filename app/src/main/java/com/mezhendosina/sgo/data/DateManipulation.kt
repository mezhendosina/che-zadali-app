package com.mezhendosina.sgo.data

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class DateManipulation(val date: String) {
    private val locale = Locale("ru", "RU")
    private val a = SimpleDateFormat("yyyy-MM-dd'T'00:00:00")

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
