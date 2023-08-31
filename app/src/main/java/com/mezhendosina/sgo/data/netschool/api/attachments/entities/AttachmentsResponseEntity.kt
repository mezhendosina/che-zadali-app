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

package com.mezhendosina.sgo.data.netschool.api.attachments.entities


import com.google.gson.annotations.SerializedName
import com.mezhendosina.sgo.app.model.answer.FileUiEntity
import com.mezhendosina.sgo.data.netschool.api.homework.entities.FileEntity

data class AttachmentsResponseEntity(
    @SerializedName("answerFiles")
    val answerFiles: List<AnswerFile>,
    @SerializedName("assignmentId")
    val assignmentId: Int,
    @SerializedName("attachments")
    val attachments: List<Attachment>
)

data class Attachment(
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("originalFileName")
    val originalFileName: String
) {
    fun toUiEntity(): FileUiEntity = FileUiEntity(
        id,
        originalFileName,
        description
    )
}

data class AnswerFile(
    val attachment: FileEntity,
    val attachmentDate: String,
    val studentId: Int
)