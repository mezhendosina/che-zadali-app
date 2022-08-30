package com.mezhendosina.sgo.data.requests.homework.entities

import com.mezhendosina.sgo.data.requests.diary.entities.Mark

data class WhyGradeEntity(
    val assignmentName: String,
    val mark: Mark,
    val typeId: Int,
)
