package com.mezhendosina.sgo.data.studentTotal


import com.google.gson.annotations.SerializedName

data class FilterSource(
    @SerializedName("defaultRange")
    val defaultRange: DefaultRange,
    @SerializedName("defaultValue")
    val defaultValue: String,
    @SerializedName("filterId")
    val filterId: String,
    @SerializedName("items")
    val items: List<Item>,
    @SerializedName("maxValue")
    val maxValue: Any,
    @SerializedName("minValue")
    val minValue: Any,
    @SerializedName("nullText")
    val nullText: Any,
    @SerializedName("range")
    val range: Range
)