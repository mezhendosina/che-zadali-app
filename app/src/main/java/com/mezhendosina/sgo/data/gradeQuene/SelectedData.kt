package com.mezhendosina.sgo.data.gradeQuene


import com.google.gson.annotations.SerializedName

data class SelectedData(
    @SerializedName("filterId")
    val filterId: String,
    @SerializedName("filterText")
    val filterText: String,
    @SerializedName("filterValue")
    val filterValue: String
)