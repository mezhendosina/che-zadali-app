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

package com.mezhendosina.sgo.app.ui.journalFlow.lessonItem

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.model.answer.FileUiEntity
import com.mezhendosina.sgo.app.netschool.base.PermissionNotGranted
import com.mezhendosina.sgo.app.netschool.base.toDescription
import com.mezhendosina.sgo.app.uiEntities.AboutLessonUiEntity
import com.mezhendosina.sgo.app.utils.toLiveData
import com.mezhendosina.sgo.data.SettingsDataStore
import com.mezhendosina.sgo.data.getValue
import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton
import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton.attachmentsRepository
import com.mezhendosina.sgo.data.netschool.repo.LessonActionListener
import com.mezhendosina.sgo.data.netschool.repo.LessonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class LessonViewModel(
    private val lessonRepository: LessonRepository = NetSchoolSingleton.lessonRepository
) : ViewModel() {

    private val _lesson = MutableLiveData<AboutLessonUiEntity>()
    val lesson = _lesson.toLiveData()

    private val _error = MutableLiveData<String>()
    val error = _error.toLiveData()

    private val lessonListener: LessonActionListener = {
        _lesson.value = it
    }

    init {
        lessonRepository.addListener(lessonListener)
    }

    suspend fun init(context: Context) {
        try {
            withContext(Dispatchers.Main) {
                _error.value = ""
            }
            lessonRepository.getAboutLesson(
                if (Singleton.lesson != null) Singleton.lesson!! else Singleton.pastMandatoryItem!!.toLessonEntity(),
                SettingsDataStore.CURRENT_USER_ID.getValue(context, -1).first()
            )
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                _error.value = e.toDescription()
            }
        }
    }

    suspend fun downloadAttachment(
        context: Context,
        attachment: FileUiEntity,
    ) {
        try {
            withContext(Dispatchers.IO) {
                attachmentsRepository.downloadAttachment(
                    context,
                    attachment.id!!,
                    attachment.fileName
                )
            }
        } catch (e: Exception) {
            if (e is PermissionNotGranted) {
                throw PermissionNotGranted()
            } else {
                _error.value = e.toDescription()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        lessonRepository.removeListener(lessonListener)
    }
}


//class LessonViewModel(
//    private val attachmentsRepository: AttachmentsRepository = NetSchoolSingleton.attachmentsRepository,
//    val answerRepository: AnswerRepository = NetSchoolSingleton.answerRepository
//) : ViewModel() {
//
//    private val _lesson = MutableLiveData<LessonUiEntity>()
//    val lesson: LiveData<LessonUiEntity> = _lesson
//
//    private val _types = MutableLiveData<List<AssignmentTypesResponseEntity>>()
//    val types: LiveData<List<AssignmentTypesResponseEntity>> = _types
//
//    private val _errorMessage = MutableLiveData<String>()
//    val errorMessage: LiveData<String> = _errorMessage
//
//    private val _snackBar = MutableLiveData(false)
//    val snackbar: LiveData<Boolean> = _snackBar
//
//
//    suspend fun init(context: Context, fullUpdate: Boolean = false) {
//        if (fullUpdate) {
//            if (Singleton.lesson != null) {
//                loadHomework(context)
//                loadGrades()
//            } else if (Singleton.pastMandatoryItem != null) {
//                loadHomework(context, Singleton.pastMandatoryItem!!.id)
//            }
//        } else {
//            if (Singleton.lesson != null) {
//                withContext(Dispatchers.Main) {
//                    _lesson.value = Singleton.lesson
//                }
//                loadGrades()
//            } else if (Singleton.pastMandatoryItem != null) {
//                withContext(Dispatchers.Main) {
//                    _lesson.value = Singleton.pastMandatoryItem!!.toLessonEntity()
//                }
//                loadHomework(context, Singleton.pastMandatoryItem!!.id)
//            }
//        }
//    }
//
//
//    fun deleteFile(context: Context, fileId: Int, onComplete: () -> Unit) {
//        CoroutineScope(Dispatchers.Main).launch {
//            try {
//                withContext(Dispatchers.IO) {
//                    val assignmentId = _lesson.value?.assignments?.find { it.typeId == 3 }?.id ?: 0
//                    attachmentsRepository.deleteAttachment(assignmentId, fileId)
//                    withContext(Dispatchers.Main) {
//                        onComplete.invoke()
//                    }
//                    loadHomework(context)
//                }
//            } catch (e: Exception) {
//                _errorMessage.value = e.toDescription()
//            }
//        }
//    }
//
//    fun loadHomework(context: Context, id: Int? = null) {
//        val settings = Settings(context)
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val homeworkId =
//                    id ?: _lesson.value?.assignments?.first { it.typeId == 3 }?.id ?: -1
//                val studentId = settings.currentUserId.first()
//                val response = homeworkSource.getAboutAssign(
//                    homeworkId,
//                    studentId
//                )
//                answerRepository.loadAnswer(studentId, homeworkId)
//                withContext(Dispatchers.Main) {
//                    _homework.value = response
//                    _attachments.value = response.attachments
//                }
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    _errorMessage.value = e.toDescription()
//                }
//            }
//        }
//    }
//
//

//}