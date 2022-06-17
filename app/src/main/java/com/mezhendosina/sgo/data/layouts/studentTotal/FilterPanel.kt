package com.mezhendosina.sgo.data.layouts.studentTotal


import com.google.gson.annotations.SerializedName

data class FilterPanel(
    @SerializedName("filters")
    val filters: List<Filter>
)