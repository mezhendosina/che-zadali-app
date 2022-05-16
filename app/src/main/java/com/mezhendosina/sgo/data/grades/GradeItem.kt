package com.mezhendosina.sgo.data.grades

data class GradeItem(
    val lessonName: String,
    val totalGrade: Double,
    val countFive: Int,
    val countFour: Int,
    val countThree: Int,
    val countTwo: Int,
    val countOne: Int
)
