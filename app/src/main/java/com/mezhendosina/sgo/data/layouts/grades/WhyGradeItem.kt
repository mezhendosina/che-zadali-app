package com.mezhendosina.sgo.data.layouts.grades

import com.mezhendosina.sgo.data.layouts.diary.diary.Mark

data class WhyGradeItem(
    val assignmentName: String,
    val mark: Mark,
    val typeId: Int,
)
