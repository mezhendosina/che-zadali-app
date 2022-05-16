package com.mezhendosina.sgo.app.ui.more

import android.content.ActivityNotFoundException
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.ui.errorDialog
import com.mezhendosina.sgo.data.ErrorResponse
import com.mezhendosina.sgo.data.attachments.Attachment
import com.mezhendosina.sgo.data.diary.diary.Lesson
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoreViewModel : ViewModel() {

    private val _lesson = MutableLiveData<Lesson>()
    val lesson: LiveData<Lesson> = _lesson

    private val _attachments = MutableLiveData<List<Attachment>>(emptyList())
    val attachments: LiveData<List<Attachment>> = _attachments

    fun findLesson(lessonId: Int, from: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val singleton = if (from == "journal") {
                Singleton.diary
            } else {
                Singleton.todayHomework
            }
//            singleton.diaryResponse.weekDays.forEach { weekDay ->
//                weekDay.lessons.forEach { lessonItem ->
//                    if (lessonItem.classmeetingId == lessonId) {
//                        val mutableAttachments = mutableListOf<Attachment>()
//                        lessonItem.assignments?.forEach { assign ->
//                            Singleton.diary.attachmentsResponse
//                                .find { it.assignmentId == assign.id }?.attachments?.forEach { attachment ->
//                                    mutableAttachments.add(attachment)
//                                }
//                            withContext(Dispatchers.Main) {
//                                _attachments.value = mutableAttachments
//                                _lesson.value = lessonItem
//                            }
//                        }
//                    }
//                }
//            }

            for (day in singleton.diaryResponse.weekDays) {
                for (lesson in day.lessons) {
                    if (lesson.classmeetingId == lessonId) {
                        val attachments = mutableListOf<Attachment>()
                        if (lesson.assignments != null) {
                            for (attachment in singleton.attachmentsResponse) {
                                for (assignment in lesson.assignments) {
                                    if (assignment.id == attachment.assignmentId) {
                                        for (i in attachment.attachments) {
                                            attachments.add(i)
                                        }
                                    }
                                }
                            }
                        }
                        withContext(Dispatchers.Main) {
                            _attachments.value = attachments
                            _lesson.value = lesson
                        }
                    }
                }
            }
        }
    }


    fun downloadAttachment(
        context: Context,
        attachment: Attachment,
        _loading: MutableLiveData<Int>
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Singleton.requests.downloadAttachment(
                    context,
                    Singleton.at,
                    attachment.id,
                    attachment.originalFileName,
                    _loading
                )
            } catch (e: ActivityNotFoundException) {
                withContext(Dispatchers.Main) {
                    _loading.value = 0
                    errorDialog(
                        context,
                        "Похоже,что на устройстве не установлено приложение для открытия этого файла"
                    )
                }
            } catch (e: ResponseException) {
                withContext(Dispatchers.Main) {
                    _loading.value = 0
                    errorDialog(
                        context,
                        e.response.body<ErrorResponse>().message
                    )
                }
            }
        }
    }
}