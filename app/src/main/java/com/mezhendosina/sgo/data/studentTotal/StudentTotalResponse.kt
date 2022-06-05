package com.mezhendosina.sgo.data.studentTotal


import com.google.gson.annotations.SerializedName

data class StudentTotalResponse(
    @SerializedName("filterSources")
    val filterSources: List<FilterSource>,
    @SerializedName("report")
    val report: Report
)