package com.mezhendosina.sgo.app.model.journal

data class DiaryModelRequestEntity(
    val studentId: Int,
    val weekStart: String,
    val weekEnd: String,
    val yearId: Int
)