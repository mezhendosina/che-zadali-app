package com.mezhendosina.sgo.data.requests.homework.entities

data class DeleteAttachmentRequestEntity(
    val assignmentId: Int,
    val assignmentFileResult: Boolean = true
)
