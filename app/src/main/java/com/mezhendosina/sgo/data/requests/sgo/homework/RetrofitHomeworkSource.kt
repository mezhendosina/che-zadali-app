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

package com.mezhendosina.sgo.data.requests.sgo.homework

import com.mezhendosina.sgo.app.model.homework.HomeworkSource
import com.mezhendosina.sgo.data.requests.sgo.base.BaseRetrofitSource
import com.mezhendosina.sgo.data.requests.sgo.base.RetrofitConfig
import com.mezhendosina.sgo.data.requests.sgo.diary.entities.AssignmentTypesResponseEntity
import com.mezhendosina.sgo.data.requests.sgo.homework.entities.*
import okhttp3.MultipartBody
import java.io.File


class RetrofitHomeworkSource(config: RetrofitConfig) :
    BaseRetrofitSource(config), HomeworkSource {

    private val homeworkSource = retrofit.create(HomeworkApi::class.java)

    override suspend fun sendTextAnswer(assignmentId: Int, studentId: Int, answer: String): Unit =
        wrapRetrofitExceptions {
            homeworkSource.sendTextAnswer(assignmentId, studentId, answer)
        }

    override suspend fun sendFileAttachment(file: MultipartBody.Part, data: SendFileRequestEntity) =
        wrapRetrofitExceptions {
            homeworkSource.sendFileAttachment(
                file,
                data
            )
        }


    override suspend fun assignmentTypes(all: Boolean): List<AssignmentTypesResponseEntity> =
        wrapRetrofitExceptions {
            homeworkSource.assignmentTypes(all)
        }

    override suspend fun getAttachments(
        studentId: Int,
        attachmentsRequestEntity: AttachmentsRequestEntity
    ): List<AttachmentsResponseEntity> =
        wrapRetrofitExceptions {
            homeworkSource.getAttachments(
                studentId,
                attachmentsRequestEntity
            )
        }

    override suspend fun getAnswer(
        assignmentId: Int,
        studentId: Int
    ): List<GetAnswerResponseEntity> =
        wrapRetrofitExceptions {
            homeworkSource.getAnswer(assignmentId, studentId)
        }

    override suspend fun getAboutAssign(assignId: Int, studentId: Int): AssignResponseEntity =
        wrapRetrofitExceptions {
            homeworkSource.getAboutAssign(assignId, studentId)
        }

    override suspend fun downloadAttachment(attachmentId: Int, file: File): String? =
        wrapRetrofitExceptions {
            val request = homeworkSource.downloadAttachment(attachmentId)
            request.body()?.byteStream()?.readBytes()
                ?.let { file.writeBytes(it) }
            return@wrapRetrofitExceptions request.headers()["Content-Type"]
        }

    override suspend fun deleteAttachment(assignmentId: Int, attachmentId: Int): Unit =
        wrapRetrofitExceptions {
            homeworkSource.deleteAttachment(
                attachmentId,
                DeleteAttachmentRequestEntity(assignmentId)
            )
        }

    override suspend fun editAttachmentDescription(attachmentId: Int, description: String): String =
        wrapRetrofitExceptions {
            homeworkSource.editAttachmentDescription(attachmentId, description)
        }
}