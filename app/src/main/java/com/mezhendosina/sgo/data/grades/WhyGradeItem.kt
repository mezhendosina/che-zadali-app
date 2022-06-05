package com.mezhendosina.sgo.data.grades

import com.mezhendosina.sgo.data.diary.diary.Mark

data class WhyGradeItem(
    val assignmentName: String,
    val mark: Mark,
    val typeId: Int,
)
