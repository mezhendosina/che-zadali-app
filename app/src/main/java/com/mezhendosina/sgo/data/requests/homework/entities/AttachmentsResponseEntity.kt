package com.mezhendosina.sgo.data.requests.homework.entities


import com.google.gson.annotations.SerializedName

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
    val description: Any,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("originalFileName")
    val originalFileName: String
)

data class AnswerFile(
    val attachment: FileUiEntity,
    val attachmentDate: String,
    val studentId: Int
)