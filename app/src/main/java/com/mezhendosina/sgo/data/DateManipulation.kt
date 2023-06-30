/*
 * Copyright 2023 Eugene Menshenin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mezhendosina.sgo.data

import android.annotation.SuppressLint
import android.content.Context
import java.text.SimpleDateFormat
import java.util.*


data class WeekStartEndEntity(
    val weekStart: String,
    val weekEnd: String,
    val formattedWeekStart: String? = null,
    val formattedWeekEnd: String? = null
)

@SuppressLint("SimpleDateFormat")
class DateManipulation(val date: String) {
    private val locale = Locale("ru", "RU")
    private val a = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(date)


    fun dateToRussianWithTime(): String = SimpleDateFormat("dd.MM.yyyy hh:mm").format(a!!)


    fun dateFormatter(): String = SimpleDateFormat("dd.MM.yyyy", locale).format(a!!)


    fun journalDate(): String = SimpleDateFormat("dd MMMM yyyyг.", locale).format(a!!)
}

@SuppressLint("SimpleDateFormat")
fun dateToRussian(date: String): String {
    val a = SimpleDateFormat("yyyy-MM-dd'T'00:00:00").parse(date)
    val locale = Locale("ru", "RU")

    return SimpleDateFormat("EEEE, d MMMM", locale).format(a!!).replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }
}

fun getWeeksList(): List<WeekStartEndEntity> {
    val outList = mutableListOf<WeekStartEndEntity>()
    val currentYearTime = currentYearTime()

    val minusWeekCalendar = Calendar.getInstance()
    minusWeekCalendar.timeInMillis = currentYearTime
    val plusWeekCalendar = Calendar.getInstance()
    plusWeekCalendar.timeInMillis = currentYearTime

    minusWeekCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    plusWeekCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    while (minusWeekCalendar[Calendar.MONTH] != 7 || plusWeekCalendar[Calendar.MONTH] != 6) {
        if (minusWeekCalendar[Calendar.MONTH] != 7) {
            minusWeekCalendar.add(Calendar.WEEK_OF_YEAR, -1)
            val weekStart = minusWeekCalendar.dateToSting()
            val weekEnd = minusWeekCalendar.getWeekEnd()
            outList.add(
                WeekStartEndEntity(
                    weekStart,
                    weekEnd,
                    tabDate(weekStart),
                    tabDate(weekEnd)
                )
            )
        }
        if (plusWeekCalendar[Calendar.MONTH] != 6) {
            val weekStart = plusWeekCalendar.dateToSting()
            val weekEnd = plusWeekCalendar.getWeekEnd()
            outList.add(
                WeekStartEndEntity(
                    weekStart,
                    weekEnd,
                    tabDate(weekStart),
                    tabDate(weekEnd)
                )
            )
            plusWeekCalendar.add(Calendar.WEEK_OF_YEAR, 1)
        }
    }

    return outList.sortedBy { it.weekStart }
}

private fun Calendar.dateToSting(): String = this.time.getDateByTime()

private fun Calendar.getWeekEnd(): String {
    this.add(Calendar.DAY_OF_YEAR, +6)
    val weekEnd = this.time.getDateByTime()
    this.add(Calendar.DAY_OF_YEAR, -6)
    return weekEnd
}

@SuppressLint("SimpleDateFormat")
fun currentYearTime(): Long {
    val s = SimpleDateFormat("MM.yyyy").format(Date().time)
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
    return SimpleDateFormat("d MMM", locale).format(s!!)
}

@SuppressLint("SimpleDateFormat")
fun currentWeekStart(context: Context? = null): String {

    val calendar = Calendar.getInstance()
//        val skipOnSunday = Settings(context).skipSunday.first()
    if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY/* && skipOnSunday == true*/) {
        calendar.add(Calendar.DAY_OF_WEEK, 1)
    } else {
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    }
    return calendar.time.getDateByTime()
}
