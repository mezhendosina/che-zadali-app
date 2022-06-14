package com.mezhendosina.sgo.app.ui.journal

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.Singleton.at
import com.mezhendosina.sgo.Singleton.requests
import com.mezhendosina.sgo.data.layouts.attachments.AttachmentsResponseItem
import com.mezhendosina.sgo.data.layouts.diary.diary.WeekDay
import com.mezhendosina.sgo.data.layouts.pastMandatory.PastMandatoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

typealias journalActionListener = (d: List<WeekDay>) -> Unit
typealias attachmentActionListener = (d: List<AttachmentsResponseItem>) -> Unit
typealias pastMandatoryActionListener = (d: List<PastMandatoryItem>) -> Unit

class JournalService {

    private var diary = mutableListOf<WeekDay>()
    private var attachments = mutableListOf<AttachmentsResponseItem>()
    private var pastMandatory = mutableListOf<PastMandatoryItem>()

    private val listeners = mutableSetOf<journalActionListener>()
    private val attachmentsListener = mutableSetOf<attachmentActionListener>()
    private val pastMandatoryListener = mutableSetOf<pastMandatoryActionListener>()

    suspend fun loadDiary(
        studentId: Int,
        yearId: Int,
        weekStartTime: Long,
        isEmpty: MutableLiveData<Boolean>
    ) {
        withContext(Dispatchers.Main) {
            isEmpty.value = false
        }
        val d = when (Singleton.diary.diaryResponse.weekDays.isEmpty()) {
            true -> {
                val diaryInit = requests.diaryInit(at)

                val a = requests.diary(
                    at,
                    diaryInit.students[0].studentId,
                    weekEndByTime(weekStartTime),
                    weekStartByTime(weekStartTime),
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
            pastMandatory = d.pastMandatory.toMutableList()
            isEmpty.value = diary.isEmpty()
            notifyListeners()
        }
    }

    suspend fun reloadDiary(
        studentId: Int,
        yearId: Int,
        weekStart: String,
        weekEnd: String,
        weekStartTime: Long,
        isEmpty: MutableLiveData<Boolean>
    ) {
        withContext(Dispatchers.Main) {
            isEmpty.value = false
        }
        val diaryInit = requests.diaryInit(at)

        val a = requests.diary(
            at,
            diaryInit.students[0].studentId,
            weekEndByTime(weekStartTime),
            weekStartByTime(weekStartTime),
            yearId
        )
        Singleton.diary = a

        withContext(Dispatchers.Main) {
            diary = a.diaryResponse.weekDays.toMutableList()
            attachments = a.attachmentsResponse.toMutableList()
            isEmpty.value = diary.isEmpty()
            notifyListeners()
        }
    }

    fun addListener(
        listener: journalActionListener,
        attachmentActionListener: attachmentActionListener,
        pastMandatoryActionListener: pastMandatoryActionListener
    ) {
        listeners.add(listener)
        attachmentsListener.add(attachmentActionListener)
        pastMandatoryListener.add(pastMandatoryActionListener)
    }

    fun removeListener(
        listener: journalActionListener,
        attachmentActionListener: attachmentActionListener,
        pastMandatoryActionListener: pastMandatoryActionListener
    ) {
        listeners.remove(listener)
        attachmentsListener.remove(attachmentActionListener)
        pastMandatoryListener.remove(pastMandatoryActionListener)
    }


    private fun notifyListeners() {
        listeners.forEach { it.invoke(diary) }
        attachmentsListener.forEach { it.invoke(attachments) }
        pastMandatoryListener.forEach { it.invoke(pastMandatory) }
    }



}