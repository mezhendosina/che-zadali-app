package com.mezhendosina.sgo.app.model.homework

import com.mezhendosina.sgo.data.requests.diary.entities.AssignmentTypesResponseEntity
import com.mezhendosina.sgo.data.requests.homework.entities.*
import java.io.File

interface HomeworkSource {
    suspend fun assignmentTypes(all: Boolean = true): List<AssignmentTypesResponseEntity>

    suspend fun getAttachments(
        studentId: Int,
        attachmentsRequestEntity: AttachmentsRequestEntity
    ): List<AttachmentsResponseEntity>

    suspend fun getAnswer(assignmentId: Int, studentId: Int): List<GetAnswerResponseEntity>

    suspend fun getAboutAssign(assignId: Int, studentId: Int): AssignResponseEntity

    suspend fun downloadAttachment(attachmentId: Int, file: File): String?

    suspend fun deleteAttachment(assignmentId: Int, attachmentId: Int)

    suspend fun editAttachmentDescription(attachmentId: Int, description: String): String

    suspend fun sendTextAnswer(
        assignmentId: Int,
        studentId: Int,
        answer: String
    )

    suspend fun sendFileAttachment(file: File, data: SendFileRequestEntity): Int
}