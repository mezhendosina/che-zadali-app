package com.mezhendosina.sgo.data.layouts.studentTotal


import com.google.gson.annotations.SerializedName

data class Filter(
    @SerializedName("emptyText")
    val emptyText: String,
    @SerializedName("existStateProvider")
    val existStateProvider: Boolean,
    @SerializedName("filterType")
    val filterType: String,
    @SerializedName("hasSureCheckedFlag")
    val hasSureCheckedFlag: Boolean,
    @SerializedName("hideSingleOption")
    val hideSingleOption: Boolean,
    @SerializedName("hideTitleFlag")
    val hideTitleFlag: Boolean,
    @SerializedName("id")
    val id: String,
    @SerializedName("optionalFlag")
    val optionalFlag: Boolean,
    @SerializedName("order")
    val order: Int,
    @SerializedName("showAllValueIfSingleFlag")
    val showAllValueIfSingleFlag: Boolean,
    @SerializedName("title")
    val title: String
)