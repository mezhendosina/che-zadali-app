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

package com.mezhendosina.sgo.app.ui.journalFlow.answer

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mezhendosina.sgo.app.model.answer.FileUiEntity
import com.mezhendosina.sgo.app.model.attachments.AttachmentDownloadManager
import com.mezhendosina.sgo.app.utils.LessonNotFoundException
import com.mezhendosina.sgo.app.utils.toLiveData
import com.mezhendosina.sgo.data.SettingsDataStore
import com.mezhendosina.sgo.data.netschool.repo.LessonRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AnswerViewModel @Inject constructor(
    private val lessonRepository: LessonRepositoryInterface,
    private val attachmentDownloadManager: AttachmentDownloadManager,
    private val settingsDataStore: SettingsDataStore
) : ViewModel(), AnswerViewModelInterface {

    private val _files = MutableLiveData<MutableList<FileUiEntity>>(mutableListOf())
    val files = _files.toLiveData()


    override fun getHomework(): String {
        val lesson = lessonRepository.lesson.value ?: throw LessonNotFoundException()
        return lesson.homework
    }


    override fun getAnswer(): String? {
        val lesson = lessonRepository.lesson.value ?: throw LessonNotFoundException()
        _files.value = lesson.answerFiles?.toMutableList()
        return lesson.answerText
    }

    fun editAnswerText(answerText: String) {
        lessonRepository.editAnswerText(answerText)
    }

    override suspend fun sendAnswer(answerText: String?) {
        TODO()
    }

    override suspend fun uploadFiles(context: Context) {
        if (_files.value != null) attachmentDownloadManager.uploadFiles(
            context,
            _files.value!!
        )
    }

    override suspend fun downloadFiles(context: Context) {
        viewModelScope.launch {
            if (settingsDataStore.getValue(SettingsDataStore.DOWNLOAD_ALL_FILES).first() == true) {
                _files.value?.forEach {
                    withContext(Dispatchers.IO) {
                        downloadFile(context, it)
                    }
                }
            }
        }
    }

    override suspend fun downloadFile(context: Context, fileUiEntity: FileUiEntity) {
        attachmentDownloadManager.downloadFile(context, fileUiEntity)
    }

    override fun openFile(context: Context, fileUiEntity: FileUiEntity) {
        attachmentDownloadManager.openFile(context, fileUiEntity)
    }

    override fun addFile(fileUiEntity: FileUiEntity) {
        _files.value?.add(fileUiEntity)
    }

    override fun deleteFile(fileUiEntity: FileUiEntity) {
        if (fileUiEntity.id == null) {
            _files.value?.remove(fileUiEntity)
        } else {
            TODO()
        }
    }

}