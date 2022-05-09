package com.mezhendosina.sgo.app.ui.main

import android.annotation.SuppressLint
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.data.diary.diary.Lesson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

typealias TodayActionListener = (a: List<Lesson>) -> Unit

@SuppressLint("SimpleDateFormat")
fun todayDate(): String {
    val calendar = Calendar.getInstance()
    val day = calendar.get(Calendar.DAY_OF_WEEK)
    val date = if (day == Calendar.SUNDAY) {
        Date().time + 86400000
    } else {
        Date().time
    }
    return SimpleDateFormat("yyyy-MM-dd").format(date)
}


class TodayHomeworkService {

    private var todayHomework = mutableListOf<Lesson>()
    private val listeners = mutableSetOf<TodayActionListener>()


    suspend fun todayHomework() {
        val date = todayDate()
        val requests = Singleton.requests
        val at = Singleton.at

        val diaryInit = requests.diaryInit(at)
        val year = requests.yearList(at).first { !it.name.contains("(*) ") }

        val diary = requests.diary(
            at,
            diaryInit.students[0].studentId,
            date,
            date,
            year.id
        )
        if (diary.weekDays.isNotEmpty()) {
            todayHomework = diary.weekDays[0].lessons.toMutableList()
            Singleton.todayHomework = diary.weekDays[0].lessons
            withContext(Dispatchers.Main) {
                notifyListeners()
            }
        }
    }

    fun addListener(listener: TodayActionListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: TodayActionListener) {
        listeners.remove(listener)
    }

    private fun notifyListeners() {
        listeners.forEach { it.invoke(todayHomework) }
    }
}