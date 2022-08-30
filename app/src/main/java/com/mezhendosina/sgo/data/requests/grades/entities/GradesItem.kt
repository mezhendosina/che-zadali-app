package com.mezhendosina.sgo.data.requests.grades.entities

data class GradesItem(
    val name: String,
    val five: Int?,
    val four: Int?,
    val two: Int?,
    val three: Int?,
    val one: Int?,
    val avg: String?
)