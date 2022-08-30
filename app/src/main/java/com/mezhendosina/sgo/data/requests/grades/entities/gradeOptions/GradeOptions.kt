package com.mezhendosina.sgo.data.requests.grades.entities.gradeOptions

data class GradeOptions(
    val PCLID: InputTag,
    val ReportType: List<SelectTag>,
    val SID: InputTag,
    val TERMID: List<SelectTag>
)