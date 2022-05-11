package com.mezhendosina.sgo.app.ui.journal

import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.Singleton.at
import com.mezhendosina.sgo.Singleton.requests
import com.mezhendosina.sgo.data.attachments.AttachmentsResponseItem
import com.mezhendosina.sgo.data.diary.diary.WeekDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

typealias journalActionListener = (d: List<WeekDay>) -> Unit
typealias attachmentActionListener = (d: List<AttachmentsResponseItem>) -> Unit

class JournalService {

    private var diary = mutableListOf<WeekDay>()
    private var attachments = mutableListOf<AttachmentsResponseItem>()

    private val listeners = mutableSetOf<journalActionListener>()
    private val attachmentsListener = mutableSetOf<(List<AttachmentsResponseItem>) -> Unit>()

    suspend fun loadDiary(studentId: Int, yearId: Int, weekStart: String, weekEnd: String) {
        val d = when (Singleton.diary.diaryResponse.weekDays.isEmpty()) {
            true -> {
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
            false -> Singleton.diary
        }
        withContext(Dispatchers.Main) {
            diary = d.diaryResponse.weekDays.toMutableList()
            attachments = d.attachmentsResponse.toMutableList()
            notifyListeners()
        }
    }

    suspend fun reloadDiary(studentId: Int, yearId: Int, weekStart: String, weekEnd: String) {

        val diaryInit = requests.diaryInit(at)

        val a = requests.diary(
            at,
            diaryInit.students[0].studentId,
            weekEnd,
            weekStart,
            yearId
        )
        Singleton.diary = a

        withContext(Dispatchers.Main) {
            diary = a.diaryResponse.weekDays.toMutableList()
            attachments = a.attachmentsResponse.toMutableList()
            notifyListeners()
        }
    }

    fun addListener(
        listener: journalActionListener,
        attachmentActionListener: attachmentActionListener
    ) {
        listeners.add(listener)
        attachmentsListener.add(attachmentActionListener)

    }

    fun removeListener(
        listener: journalActionListener,
        attachmentActionListener: attachmentActionListener
    ) {
        listeners.remove(listener)
        attachmentsListener.remove(attachmentActionListener)
    }


    private fun notifyListeners() {
        listeners.forEach { it.invoke(diary) }
        attachmentsListener.forEach { it.invoke(attachments) }
    }

}