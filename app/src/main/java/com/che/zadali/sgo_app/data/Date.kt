package com.che.zadali.sgo_app.data

import android.annotation.SuppressLint
import com.che.zadali.sgo_app.data.diary.Diary
import com.che.zadali.sgo_app.data.diary.WeekDay
import java.text.SimpleDateFormat
import java.util.*

//Prepare date
@SuppressLint("SimpleDateFormat")
fun dateToRussian(date: String, uppercase:Boolean): String {
    val a = SimpleDateFormat("yyyy-MM-dd'T'00:00:00").parse(date)
    return  when(uppercase){
        true -> SimpleDateFormat("EEEE, dd MMMM").format(a!!).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        false ->  SimpleDateFormat("Сегодня EE, dd MMMM").format(a!!)
    }
}

//Format date
@SuppressLint("SimpleDateFormat")
fun dateFormatter(date: String): String {
    val a = SimpleDateFormat("yyyy-MM-dd'T'00:00:00").parse(date)
    return SimpleDateFormat("dd.MM.yyyy").format(a!!)
}

@SuppressLint("SimpleDateFormat")
fun today(): String {
    val date = Date()
    return SimpleDateFormat("yyyy-MM-dd'T'00:00:00").format(date)
}

//Days for holiday
@SuppressLint("SimpleDateFormat")
fun whenHoliday(holidayStart: String): String {
    val a = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val diff = System.currentTimeMillis() - a.parse(holidayStart)!!.time
    return (-diff / (24 * 60 * 60 * 1000)).toString()
}

fun todayHomework(diary: Diary): List<WeekDay> {//TODO переписать null check
    var a = diary.weekDays.filter { it.date == today() }
    if(a.isEmpty()){
        a = diary.weekDays
    }
    return a
}