package com.mezhendosina.sgo.mobile.data

data class SubjectTotal(
    val subjectId: Int,
    val termTotals: List<TermTotal>,
    val yearTotals: List<YearTotal>,
)
