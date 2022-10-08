package com.mezhendosina.sgo.data

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*


data class WeekStartEndEntity(
    val weekStart: String,
    val weekEnd: String
) {
    fun toUI(): WeekStartEndEntity =
        WeekStartEndEntity(
            dateToJournalDate(this.weekStart),
            dateToJournalDate(this.weekEnd)
        )

}

@SuppressLint("SimpleDateFormat")
class DateManipulation(val date: String) {
    private val locale = Locale("ru", "RU")
    private val a = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(date)

    fun dateToRussian(uppercase: Boolean): String = when (uppercase) {
        true -> SimpleDateFormat("EEEE, dd MMMM", locale).format(a!!)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        false -> SimpleDateFormat("Сегодня EE, dd MMMM", locale).format(a!!)
    }


    fun dateToRussianWithTime(): String = SimpleDateFormat("dd.MM.yyyy hh:mm").format(a!!)


    fun dateFormatter(): String = SimpleDateFormat("dd.MM.yyyy", locale).format(a!!)


    fun journalDate(): String = SimpleDateFormat("dd MMMM yyyyг.", locale).format(a!!)


    fun messageDate(): String {
        val a = SimpleDateFormat("yyyy-MM-dd'T'00:00:00").parse(date)
        return SimpleDateFormat("dd MMMM", locale).format(a!!)
    }

}

@SuppressLint("SimpleDateFormat")
fun currentTime(): Long {
    val s = SimpleDateFormat("w.yyyy").format(Date().time)
    return SimpleDateFormat("w.yyyy").parse(s)!!.time

}


@SuppressLint("SimpleDateFormat")
fun dateToRussian(date: String): String {
    val a = SimpleDateFormat("yyyy-MM-dd'T'00:00:00").parse(date)
    val locale = Locale("ru", "RU")

    return SimpleDateFormat("EEEE, dd MMMM", locale).format(a!!).replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()
        ) else it.toString()
    }
}

@SuppressLint("SimpleDateFormat")
fun dateToJournalDate(string: String): String {
    val a = SimpleDateFormat("yyyy-MM-dd").parse(string)
    val locale = Locale("ru", "RU")

    return SimpleDateFormat("dd MMMM yyyyг.", locale).format(a!!)
}

fun getWeeksList(): List<WeekStartEndEntity> {
    val curr = currentYearTime()
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = curr
    calendar.set(Calendar.DAY_OF_MONTH, 1)

    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

    calendar.add(Calendar.YEAR, +1)
    val nextYear = calendar.time

    calendar.add(Calendar.YEAR, -1)

    val outputList = mutableListOf<WeekStartEndEntity>()

    println(calendar.dateToSting())
    while (nextYear.after(calendar.time)) {
        outputList.add(
            WeekStartEndEntity(
                calendar.dateToSting(),
                calendar.getWeekEnd(),
            )
        )
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        println(outputList)

    }
    println(outputList)
    return outputList
}

private fun Calendar.dateToSting(): String = this.time.getDateByTime()

private fun Calendar.getWeekEnd(): String {
    this.add(Calendar.DAY_OF_MONTH, +6)
    return this.time.getDateByTime()
}

@SuppressLint("SimpleDateFormat")
fun currentYearTime(): Long {
    val s = SimpleDateFormat("09.yyyy").format(Date().time)
    val a = SimpleDateFormat("MM.yyyy").parse(s)!!
    return a.time
}

@SuppressLint("SimpleDateFormat")
private fun Date.getDateByTime(): String {
    return SimpleDateFormat("yyyy-MM-dd").format(this)
}

@SuppressLint("SimpleDateFormat")
fun tabDate(date: String): String {
    val locale = Locale("ru", "RU")

    val s = SimpleDateFormat("yyyy-MM-dd").parse(date)
    return SimpleDateFormat("dd MMM", locale).format(s!!)
}

@SuppressLint("SimpleDateFormat")
fun currentWeekStart(): String {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    return calendar.time.getDateByTime()
}
