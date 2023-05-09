/*
 * Copyright 2023 Eugene Menshenin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mezhendosina.sgo.app.ui.lessonItem

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.databinding.ItemAttachmentBinding
import com.mezhendosina.sgo.app.model.answer.AnswerRepository
import com.mezhendosina.sgo.app.model.attachments.AttachmentsRepository
import com.mezhendosina.sgo.app.model.homework.HomeworkSource
import com.mezhendosina.sgo.app.model.journal.entities.LessonUiEntity
import com.mezhendosina.sgo.app.utils.PermissionNotGranted
import com.mezhendosina.sgo.app.utils.toDescription
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.requests.sgo.diary.entities.AssignmentTypesResponseEntity
import com.mezhendosina.sgo.data.requests.sgo.homework.entities.AssignResponseEntity
import com.mezhendosina.sgo.data.requests.sgo.homework.entities.Attachment
import com.mezhendosina.sgo.data.requests.sgo.homework.entities.WhyGradeEntity
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


    suspend fun init(context: Context) {
        if (Singleton.lesson != null) {
            withContext(Dispatchers.Main) {
                _lesson.value = Singleton.lesson
            }
            loadGrades()
        } else if (Singleton.pastMandatoryItem != null) {
            withContext(Dispatchers.Main) {
                _lesson.value = Singleton.pastMandatoryItem!!.toLessonEntity()
            }
            loadHomework(context, Singleton.pastMandatoryItem!!.id)
        }
    }

    fun downloadAttachment(
        context: Context,
        attachment: Attachment,
        binding: ItemAttachmentBinding
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                withContext(Dispatchers.IO) {
                    attachmentsRepository.downloadAttachment(
                        context,
                        attachment.id,
                        attachment.originalFileName
                    )
                }
            } catch (e: Exception) {
                _errorMessage.value = e.toDescription()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    fun downloadFile(context: Context, fileId: Int, name: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                withContext(Dispatchers.IO) {
                    attachmentsRepository.downloadAttachment(
                        context,
                        fileId,
                        name
                    )
                }
            } catch (e: Exception) {
                _errorMessage.value = e.toDescription()
            }
        }
    }

    fun deleteFile(context: Context, fileId: Int, onComplete: () -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                withContext(Dispatchers.IO) {
                    val assignmentId = _lesson.value?.assignments?.find { it.typeId == 3 }?.id ?: 0
                    attachmentsRepository.deleteAttachment(assignmentId, fileId)
                    withContext(Dispatchers.Main) {
                        onComplete.invoke()
                    }
                    loadHomework(context)
                }
            } catch (e: Exception) {
                _errorMessage.value = e.toDescription()
            }
        }
    }


    fun sendAnswer(context: Context, answer: String) {
        val settings = Settings(context)
        val assignmentId = if (Singleton.lesson != null) {
            _lesson.value?.assignments?.find { it.typeId == 3 }!!.id
        } else {
            Singleton.pastMandatoryItem!!.id
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val studentId = settings.currentUserId.first()
                homeworkSource.sendTextAnswer(
                    assignmentId,
                    studentId,
                    answer
                )
                val newAnswer = homeworkSource.getAnswer(assignmentId, studentId)
                withContext(Dispatchers.Main) {
                    _answerFiles.value = newAnswer
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = e.toDescription()
                }
            }
        }
    }

    fun loadHomework(id: Int? = null) {
        val settings = Settings(Singleton.getContext())
        if (id == null) {
            _lesson.value?.assignments?.forEach {
                if (it.typeId == 3) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val studentId = settings.currentUserId.first()
                            val response = homeworkSource.getAboutAssign(
                                it.id,
                                studentId
                            )
                            val answerFiles = homeworkSource.getAnswer(
                                it.id,
                                studentId
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
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val studentId = settings.currentUserId.first()
                    val response = homeworkSource.getAboutAssign(
                        id,
                        studentId
                    )
                    val answerFiles = homeworkSource.getAnswer(
                        id,
                        studentId
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


    private suspend fun loadGrades() {
        try {
            val gradesList = mutableListOf<WhyGradeEntity>()
            val types = homeworkSource.assignmentTypes()
            _lesson.value?.assignments?.forEach {
                if (it.mark != null) {
                    gradesList.add(
                        WhyGradeEntity(
                            it.assignmentName,
                            it.mark,
                            it.markComment,
                            it.typeId
                        )
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