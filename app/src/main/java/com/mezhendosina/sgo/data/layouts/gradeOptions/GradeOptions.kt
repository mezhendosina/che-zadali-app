package com.mezhendosina.sgo.data.layouts.gradeOptions

data class GradeOptions(
    val PCLID: PCLID,
    val ReportType: List<ReportType>,
    val SID: SID,
    val TERMID: List<TERMID>
)