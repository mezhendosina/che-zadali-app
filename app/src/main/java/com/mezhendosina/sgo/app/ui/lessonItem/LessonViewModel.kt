package com.mezhendosina.sgo.app.ui.lessonItem

import android.content.ActivityNotFoundException
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.map
import com.google.android.material.snackbar.Snackbar
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.databinding.LessonItemBinding
import com.mezhendosina.sgo.app.ui.errorDialog
import com.mezhendosina.sgo.data.ErrorResponse
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.layouts.assignRequest.AssignResponse
import com.mezhendosina.sgo.data.layouts.attachments.Attachment
import com.mezhendosina.sgo.data.layouts.diary.Diary
import com.mezhendosina.sgo.data.layouts.diary.diary.Lesson
import com.mezhendosina.sgo.data.layouts.grades.WhyGradeItem
import com.mezhendosina.sgo.data.layouts.homeworkTypes.TypesResponseItem
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LessonViewModel(private val lessonService: LessonService) : ViewModel() {

    private val _lesson = MutableLiveData<Lesson>()
    val lesson: LiveData<Lesson> = _lesson

    private val _homework = MutableLiveData<AssignResponse>()
    val homework: LiveData<AssignResponse> = _homework

    private val _grades = MutableLiveData<List<WhyGradeItem>>()
    val grades: LiveData<List<WhyGradeItem>> = _grades

    private val _types = MutableLiveData<List<TypesResponseItem>>()
    val types: LiveData<List<TypesResponseItem>> = _types

    private val _attachments = MutableLiveData<List<Attachment>>(emptyList())
    val attachments: LiveData<List<Attachment>> = _attachments


    fun findLesson(lessonId: Int, from: String) {
        CoroutineScope(Dispatchers.IO).launch {
            if (from == "journal") {
                mapLesson(Singleton.diary, lessonId)
            } else {
                mapLesson(Singleton.todayHomework, lessonId)
            }
        }
    }

    suspend fun mapLesson(diary: Diary, lessonId: Int) {
        for (day in diary.diaryResponse.weekDays) {
            for (lesson in day.lessons) {
                if (lesson.classmeetingId == lessonId) {
                    val attachments = mutableListOf<Attachment>()
                    if (lesson.assignments != null) {
                        for (attachment in diary.attachmentsResponse) {
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

    fun sendAnswer(context: Context, answer: String, binding: LessonItemBinding) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val settings = Settings(context)
                val assignment = _lesson.value?.assignments?.find { it.typeId == 3 }
                Singleton.requests.sendAnswer(
                    Singleton.at,
                    settings.currentUserId.first(),
                    assignment?.id!!,
                    answer
                )
                withContext(Dispatchers.Main) {
                    Snackbar.make(binding.root, "Ответ прикреплен", Snackbar.LENGTH_LONG).show()
                }
            } catch (e: ResponseException) {
                withContext(Dispatchers.Main) {
                    errorDialog(context, e.response.body())
                }
            }
        }
    }

    suspend fun loadHomework(context: Context) {
        val settings = Settings(context)
        _lesson.value?.assignments?.forEach {
            if (it.typeId == 3) {
                try {
                    val response = Singleton.requests.loadAssign(
                        Singleton.at,
                        settings.currentUserId.first(),
                        it.id
                    )
                    withContext(Dispatchers.Main) {
                        _homework.value = response
                    }
                } catch (e: ResponseException) {
                    withContext(Dispatchers.Main) {
                        errorDialog(context, e.response.body())
                    }
                }
            }
        }
    }

    suspend fun loadGrades(context: Context) {
        try {
            val gradesList = mutableListOf<WhyGradeItem>()
            val types = Singleton.requests.loadTypes()
            _lesson.value?.assignments?.forEach {
                if (it.mark != null) {
                    gradesList.add(
                        WhyGradeItem(it.assignmentName, it.mark, it.typeId)
                    )
                }
            }
            withContext(Dispatchers.Main) {
                _types.value = types
                _grades.value = gradesList
            }
        } catch (e: ResponseException) {
            withContext(Dispatchers.Main) {
                errorDialog(context, e.response.body())
            }
        }
    }
}