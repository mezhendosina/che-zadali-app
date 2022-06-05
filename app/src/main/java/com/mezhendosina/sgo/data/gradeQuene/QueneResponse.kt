package com.mezhendosina.sgo.data.gradeQuene


import com.google.gson.annotations.SerializedName

data class QueneResponse(
    @SerializedName("params")
    val params: List<Param>,
    @SerializedName("selectedData")
    val selectedData: List<SelectedData>
)