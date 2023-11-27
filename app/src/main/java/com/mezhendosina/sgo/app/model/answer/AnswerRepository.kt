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

package com.mezhendosina.sgo.app.model.answer

import android.content.Context
import android.net.Uri
import com.mezhendosina.sgo.app.utils.getFileNameFromUri
import com.mezhendosina.sgo.data.netschool.api.attachments.AttachmentsSource
import com.mezhendosina.sgo.data.netschool.api.attachments.entities.SendFileRequestEntity
import com.mezhendosina.sgo.data.netschool.repo.LessonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class AnswerRepository @Inject constructor(
    private val attachmentsSource: AttachmentsSource,
    private val lessonRepository: LessonRepository
) {

    suspend fun sendTextAnswer(assignId: Int, text: String, studentId: Int) {
        attachmentsSource.sendTextAnswer(assignId, studentId, text)
    }

    suspend fun sendFiles(
        context: Context,
        files: List<FileUiEntity>,
        assignId: Int
    ): List<FileUiEntity> {
        lessonRepository.getLesson()?.answerFiles?.forEach {
            if (!files.contains(it) && it.id != null) {
                attachmentsSource.deleteAttachment(assignId, it.id)
            }
        }
        val out = files.map {
            if (it.file != null) {
                val fileId = sendFile(context, assignId, it.file, it.description)
                it.addId(fileId)
            } else {
                it
            }
        }
        return out
    }


    private suspend fun sendFile(
        context: Context,
        assignmentID: Int,
        filePath: Uri,
        description: String?
    ): Int? {
        val contentResolver = context.contentResolver
        val a = contentResolver.openInputStream(filePath)

        val body = a?.readBytes()?.toRequestBody("*/*".toMediaTypeOrNull())
        val fileName = getFileNameFromUri(context, filePath)
        val out = if (body != null) {
            val part = MultipartBody.Part.createFormData("file", fileName, body)
            attachmentsSource.sendFileAttachment(
                part,
                SendFileRequestEntity(
                    true,
                    assignmentID,
                    description,
                    fileName ?: ""
                )
            )
        } else null
        withContext(Dispatchers.IO) { a?.close() }
        return out
    }
}