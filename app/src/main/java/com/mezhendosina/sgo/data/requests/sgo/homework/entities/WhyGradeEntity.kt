package com.mezhendosina.sgo.data.requests.sgo.homework.entities

import com.mezhendosina.sgo.data.requests.sgo.diary.entities.Mark

data class WhyGradeEntity(
    val assignmentName: String,
    val mark: Mark,
    val typeId: Int,
)
