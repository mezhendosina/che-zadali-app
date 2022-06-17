package com.mezhendosina.sgo.data.layouts.gradeQuene


import com.google.gson.annotations.SerializedName

data class QueneResponse(
    @SerializedName("params")
    val params: List<Param>,
    @SerializedName("selectedData")
    val selectedData: List<SelectedData>
)