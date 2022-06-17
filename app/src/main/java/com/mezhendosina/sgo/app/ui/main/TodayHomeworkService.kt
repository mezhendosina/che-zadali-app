package com.mezhendosina.sgo.app.ui.main

import android.annotation.SuppressLint
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.data.layouts.attachments.AttachmentsResponseItem
import com.mezhendosina.sgo.data.layouts.diary.diary.Lesson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

typealias TodayActionListener = (a: List<Lesson>) -> Unit
typealias TodayAttachmentsListener = (a: List<AttachmentsResponseItem>) -> Unit


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
    private var todayAttachments = mutableListOf<AttachmentsResponseItem>()
    private val attachmentsListeners = mutableSetOf<TodayAttachmentsListener>()

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
        if (diary.diaryResponse.weekDays.isNotEmpty()) {
            todayHomework = diary.diaryResponse.weekDays[0].lessons.toMutableList()
            Singleton.todayHomework = diary
            if (diary.attachmentsResponse.isNotEmpty()) {
                todayAttachments = diary.attachmentsResponse.toMutableList()
            }
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

    fun addAttachmentsListener(listener: TodayAttachmentsListener) {
        attachmentsListeners.add(listener)
    }

    fun removeAttachmentsListener(listener: TodayAttachmentsListener) {
        attachmentsListeners.remove(listener)
    }

    private fun notifyListeners() {
        listeners.forEach { it.invoke(todayHomework) }
        attachmentsListeners.forEach { it.invoke(todayAttachments) }
    }
}