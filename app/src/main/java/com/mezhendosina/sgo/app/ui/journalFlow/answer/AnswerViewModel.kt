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
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.model.answer.FileUiEntity
import com.mezhendosina.sgo.app.model.attachments.AttachmentDownloadManager
import com.mezhendosina.sgo.app.model.attachments.AttachmentsRepository
import com.mezhendosina.sgo.app.model.attachments.AttachmentsUtils
import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton
import com.mezhendosina.sgo.data.netschool.repo.LessonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AnswerViewModel @Inject constructor(
    private val lessonRepository: LessonRepository,
    private val attachmentDownloadManager: AttachmentDownloadManager,
) : ViewModel() {


    fun getAnswerText(): String {
        return lessonRepository.answerText
    }

    fun getAnswerFiles(): List<FileUiEntity> {
        return lessonRepository.answerFiles
    }

    fun getHomework() =
        if (Singleton.lesson?.homework != null) Singleton.lesson!!.homework!!.assignmentName
        else if (Singleton.pastMandatoryItem != null) Singleton.pastMandatoryItem!!.assignmentName
        else null

    suspend fun openFile(context: Context, fileUiEntity: FileUiEntity) {

    }

    fun addFile(fileUiEntity: FileUiEntity) {
        lessonRepository.answerFiles = lessonRepository.answerFiles.plus(fileUiEntity)
    }

    fun deleteFile(fileUiEntity: FileUiEntity) {
        lessonRepository.answerFiles = lessonRepository.answerFiles.minus(fileUiEntity)
    }

    fun editTextAnswer(text: String) {
        lessonRepository.answerText = text
    }
}