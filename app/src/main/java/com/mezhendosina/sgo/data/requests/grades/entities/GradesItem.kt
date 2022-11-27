package com.mezhendosina.sgo.data.requests.grades.entities

import com.mezhendosina.sgo.data.grades.CalculateGradeItem

data class GradesItem(
    val name: String,
    val five: Int?,
    val four: Int?,
    val three: Int?,
    val two: Int?,
    val one: Int?,
    val avg: String?
) {
    fun avgGrade(): Float = avg?.replace(",", ".")?.toFloat() ?: 0f

    fun countGrades(): Int = (five ?: 0) + (four ?: 0) + (three ?: 0) + (two ?: 0) + (one ?: 0)

    fun toCalculateItem(): CalculateGradeItem = CalculateGradeItem(
        five ?: 0,
        four ?: 0,
        three ?: 0,
        two ?: 0
    )

}