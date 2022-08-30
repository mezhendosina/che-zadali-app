package com.mezhendosina.sgo.data.requests.homework.entities

data class SendFileRequestEntity(
    val assignmentFileResult: Boolean,
    val assignmentId: Int,
    val description: String,
    val name: String
)