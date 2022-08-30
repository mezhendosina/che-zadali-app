package com.mezhendosina.sgo.data.requests.diary.entities

data class DiaryRequestEntity(
    val studentId: Int,
    val weekEnd: String,
    val weekStart: String,
    val withLaAssigns: Boolean,
    val yearId: Int
)
