package com.mezhendosina.sgo.data.requests.sgo.homework.entities

import com.mezhendosina.sgo.data.requests.sgo.diary.entities.TextAnswer

data class GetAnswerResponseEntity(
    val files: List<FileUiEntity>,
    val studentId: Int,
    val text: TextAnswer?
)

data class FileUiEntity(
    val aFile: Any,
    val attachmentDate: String,
    val description: String?,
    val fileName: String,
    val id: Int,
    val saved: Int,
    val userId: Int?
)