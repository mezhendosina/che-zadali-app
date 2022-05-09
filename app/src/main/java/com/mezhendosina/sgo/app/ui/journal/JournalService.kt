package com.mezhendosina.sgo.app.ui.journal

import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.Singleton.at
import com.mezhendosina.sgo.Singleton.requests
import com.mezhendosina.sgo.data.diary.diary.WeekDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

typealias journalActionListener = (d: List<WeekDay>) -> Unit


class JournalService {

    private var diary = mutableListOf<WeekDay>()

    private val listeners = mutableSetOf<journalActionListener>()


    suspend fun loadDiary(studentId: Int, yearId: Int, weekStart: String, weekEnd: String) {
        val d = if (Singleton.diary.weekDays.isNotEmpty()) {
            Singleton.diary
        } else {
            val diaryInit = requests.diaryInit(at)

            val a = requests.diary(
                at,
                diaryInit.students[0].studentId,
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

    private fun notifyListeners() {
        listeners.forEach { it.invoke(diary) }
    }

}