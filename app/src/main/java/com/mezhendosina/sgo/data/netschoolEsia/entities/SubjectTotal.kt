package com.mezhendosina.sgo.data.netschoolEsia.entities

data class SubjectTotal(
    val subjectId: Int,
    val termTotals: List<TermTotal>,
    val yearTotals: List<YearTotal>,
)
