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
import androidx.lifecycle.viewModelScope
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.model.answer.FileUiEntity
import com.mezhendosina.sgo.app.model.attachments.AttachmentDownloadManagerInterface
import com.mezhendosina.sgo.app.uiEntities.AboutLessonUiEntity
import com.mezhendosina.sgo.app.utils.toLiveData
import com.mezhendosina.sgo.data.SettingsDataStore
import com.mezhendosina.sgo.data.netschool.base.PermissionNotGranted
import com.mezhendosina.sgo.data.netschool.base.toDescription
import com.mezhendosina.sgo.data.netschool.repo.LessonActionListener
import com.mezhendosina.sgo.data.netschool.repo.LessonRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LessonViewModel
@Inject constructor(
    private val lessonRepository: LessonRepositoryInterface,
    private val attachmentDownloadManager: AttachmentDownloadManagerInterface,
    private val settingsDataStore: SettingsDataStore
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
        viewModelScope.launch {
            loadLesson()
        }
    }

    suspend fun changePermissionStatus(status: Boolean?) {
        attachmentDownloadManager.changePermissionStatus(status)
    }

    suspend fun loadLesson() {
        try {
            withContext(Dispatchers.Main) {
                _error.value = ""
            }
            lessonRepository.getAboutLesson(
                if (Singleton.lesson != null) Singleton.lesson!! else Singleton.pastMandatoryItem!!.toLessonEntity(),
                settingsDataStore.getValue(SettingsDataStore.CURRENT_USER_ID).first() ?: -1
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
            attachmentDownloadManager.doAfterGetPermission(context) {
                withContext(Dispatchers.IO) {
                    attachmentDownloadManager.downloadFile(
                        context,
                        attachment
                    )
                    withContext(Dispatchers.Main) {
                        attachmentDownloadManager.openFile(
                            context,
                            attachment
                        )
                    }
                }
            }

        } catch (e: Exception) {
            if (e is PermissionNotGranted) {
                throw PermissionNotGranted()
            } else {
                withContext(Dispatchers.Main) {
                    _error.value = e.toDescription()
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        lessonRepository.removeListener(lessonListener)
    }
}