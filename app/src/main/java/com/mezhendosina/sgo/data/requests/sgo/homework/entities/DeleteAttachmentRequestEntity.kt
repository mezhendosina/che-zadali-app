package com.mezhendosina.sgo.data.requests.sgo.homework.entities

data class DeleteAttachmentRequestEntity(
    val assignmentId: Int,
    val assignmentFileResult: Boolean = true
)
