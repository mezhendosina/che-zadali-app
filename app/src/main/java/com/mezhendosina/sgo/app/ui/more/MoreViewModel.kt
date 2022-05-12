package com.mezhendosina.sgo.app.ui.more

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.data.attachments.Attachment
import com.mezhendosina.sgo.data.diary.diary.Lesson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoreViewModel : ViewModel() {

    private val _lesson = MutableLiveData<Lesson>()
    val lesson: LiveData<Lesson> = _lesson

    private val _attachments = MutableLiveData<List<Attachment>>(emptyList())
    val attachments: LiveData<List<Attachment>> = _attachments


    private val _loading = MutableLiveData<Int>(0)
    val loading: LiveData<Int> = _loading

    fun findLesson(lessonId: Int, from: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val singleton = if (from == "journal") {
                Singleton.diary
            } else {
                Singleton.todayHomework
            }
            singleton.diaryResponse.weekDays.forEach { weekDay ->
                weekDay.lessons.forEach { lessonItem ->
                    if (lessonItem.classmeetingId == lessonId) {
                        val mutableAttachments = mutableListOf<Attachment>()
                        lessonItem.assignments?.forEach { assign ->
                            Singleton.diary.attachmentsResponse
                                .find { it.assignmentId == assign.id }?.attachments?.forEach { attachment ->
                                    mutableAttachments.add(attachment)
                                }
                            withContext(Dispatchers.Main) {
                                _attachments.value = mutableAttachments
                                _lesson.value = lessonItem
                            }
                        }
                    }
                }
            }
        }
    }


    fun downloadAttachment(context: Context, attachment: Attachment) {
        CoroutineScope(Dispatchers.IO).launch {
            Singleton.requests.downloadAttachment(
                context,
                Singleton.at,
                attachment.id,
                attachment.originalFileName,
                _loading
            )
        }
    }
}