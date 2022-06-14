package com.mezhendosina.sgo.data.layouts.queneRequest


import com.google.gson.annotations.SerializedName

data class QueneRequest(
    @SerializedName("params")
    val params: List<Param>,
    @SerializedName("selectedData")
    val selectedData: List<SelectedData>
)