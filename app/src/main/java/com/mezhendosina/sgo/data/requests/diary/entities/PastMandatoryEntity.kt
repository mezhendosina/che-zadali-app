package com.mezhendosina.sgo.data.requests.diary.entities

data class PastMandatoryEntity(
    val id: Int,
    val subjectName: String,
    val assignmentName: String,
    val dueDate: String
)
