package com.mezhendosina.sgo.data.requests.homework.entities

import com.mezhendosina.sgo.data.requests.diary.entities.TextAnswer

data class GetAnswerResponseEntity(
    val files: List<File>,
    val studentId: Int,
    val text: TextAnswer?
)

data class File(
    val aFile: Any,
    val attachmentDate: String,
    val description: String?,
    val fileName: String,
    val id: Int,
    val saved: Int,
    val userId: Int?
)