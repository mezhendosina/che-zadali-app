package com.mezhendosina.sgo.data.studentTotal


import com.google.gson.annotations.SerializedName

data class Report(
    @SerializedName("filterPanel")
    val filterPanel: FilterPanel,
    @SerializedName("group")
    val group: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("level")
    val level: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("order")
    val order: Int,
    @SerializedName("presentTypes")
    val presentTypes: List<Any>
)