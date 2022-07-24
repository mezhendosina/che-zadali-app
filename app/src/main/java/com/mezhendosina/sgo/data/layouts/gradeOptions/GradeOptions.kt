package com.mezhendosina.sgo.data.layouts.gradeOptions

data class GradeOptions(
    val PCLID: InputTag,
    val ReportType: List<SelectTag>,
    val SID: InputTag,
    val TERMID: List<SelectTag>
)