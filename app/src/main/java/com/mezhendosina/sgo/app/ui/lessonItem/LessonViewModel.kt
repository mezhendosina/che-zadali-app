package com.mezhendosina.sgo.app.ui.lessonItem

import android.content.ActivityNotFoundException
import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.databinding.ItemAttachmentBinding
import com.mezhendosina.sgo.app.model.attachments.AttachmentsRepository
import com.mezhendosina.sgo.app.model.homework.HomeworkSource
import com.mezhendosina.sgo.app.model.journal.entities.LessonUiEntity
import com.mezhendosina.sgo.app.toDescription
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.requests.homework.entities.WhyGradeEntity
import com.mezhendosina.sgo.data.requests.diary.entities.AssignmentTypesResponseEntity
import com.mezhendosina.sgo.data.requests.homework.entities.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LessonViewModel(
    private val attachmentsRepository: AttachmentsRepository = Singleton.attachmentsRepository,
    private val homeworkSource: HomeworkSource = Singleton.homeworkSource
) : ViewModel() {

    private val _lesson = MutableLiveData<LessonUiEntity>()
    val lesson: LiveData<LessonUiEntity> = _lesson

    private val _homework = MutableLiveData<AssignResponseEntity>()
    val homework: LiveData<AssignResponseEntity> = _homework

    private val _grades = MutableLiveData<List<WhyGradeEntity>>()
    val grades: LiveData<List<WhyGradeEntity>> = _grades

    private val _types = MutableLiveData<List<AssignmentTypesResponseEntity>>()
    val types: LiveData<List<AssignmentTypesResponseEntity>> = _types

    private val _attachments = MutableLiveData<List<Attachment>>()
    val attachments: LiveData<List<Attachment>> = _attachments

    private val _answerFiles = MutableLiveData<List<GetAnswerResponseEntity>>()
    val answerFiles: LiveData<List<GetAnswerResponseEntity>> = _answerFiles

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _snackBar = MutableLiveData(false)
    val snackbar: LiveData<Boolean> = _snackBar


    init {
        _lesson.value = Singleton.lesson
        viewModelScope.launch { loadGrades() }
    }


    fun downloadAttachment(
        context: Context,
        attachment: Attachment,
        binding: ItemAttachmentBinding
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                withContext(Dispatchers.IO) {
                    attachmentsRepository.downloadAttachment(context, attachment)
                }
            } catch (e: Exception) {
                _errorMessage.value =
                    if (e is ActivityNotFoundException) "Похоже, что на устройстве не установлено приложение для открытия этого файла"
                    else e.toDescription()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    fun sendAnswer(context: Context, answer: String) {
        val settings = Settings(context)
        val assignment = _lesson.value?.assignments?.find { it.typeId == 3 }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                assignment?.id?.let {
                    homeworkSource.sendTextAnswer(
                        it,
                        settings.currentUserId.first(),
                        answer
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = e.toDescription()
                }
            }
        }
    }

    fun loadHomework(context: Context) {
        val settings = Settings(context)
        _lesson.value?.assignments?.forEach {
            if (it.typeId == 3) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = homeworkSource.getAboutAssign(
                            it.id,
                            settings.currentUserId.first()
                        )
                        val answerFiles = homeworkSource.getAnswer(
                            it.id,
                            settings.currentUserId.first()
                        )
                        withContext(Dispatchers.Main) {
                            _homework.value = response
                            _answerFiles.value = answerFiles
                            _attachments.value = response.attachments
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            _errorMessage.value = e.toDescription()
                        }
                    }
                }
            }
        }
    }

    suspend fun loadGrades() {
        try {
            val gradesList = mutableListOf<WhyGradeEntity>()
            val types = homeworkSource.assignmentTypes()
            _lesson.value?.assignments?.forEach {
                if (it.mark != null) {
                    gradesList.add(
                        WhyGradeEntity(it.assignmentName, it.mark, it.typeId)
                    )
                }
            }
            withContext(Dispatchers.Main) {
                _types.value = types
                _grades.value = gradesList
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                _errorMessage.value = e.toDescription()
            }
        }
    }
}