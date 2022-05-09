package com.mezhendosina.sgo.app.ui.journal

import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.data.diary.diary.WeekDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

typealias journalActionListener = (d: List<WeekDay>) -> Unit


class JournalService {

    private var diary = mutableListOf<WeekDay>()

    private val listeners = mutableSetOf<journalActionListener>()


    suspend fun loadDiary(studentId: Int, yearId: Int, weekStart: String, weekEnd: String) {
        val d = if (Singleton.diary.weekDays.isNotEmpty()) {
            Singleton.diary
        } else {
            val a = Singleton.requests.diary(
                Singleton.at,
                studentId,
                weekEnd,
                weekStart,
                yearId
            )
            Singleton.diary = a
            a
        }
        withContext(Dispatchers.Main) {
            diary = d.weekDays.toMutableList()
            notifyListeners()
        }
    }

    fun addListener(listener: journalActionListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: journalActionListener) {
        listeners.remove(listener)
    }

    fun notifyListeners() {
        listeners.forEach { it.invoke(diary) }
    }

}