package com.mezhendosina.sgo.data.diary.diary


import com.google.gson.annotations.SerializedName
import com.mezhendosina.sgo.data.attachments.AttachmentsResponseItem

data class DiaryResponse(
    @SerializedName("className")
    val className: String,
    @SerializedName("laAssigns")
    val laAssigns: List<Any>,
    @SerializedName("termName")
    val termName: String,
    @SerializedName("weekDays")
    val weekDays: List<WeekDay>,
    @SerializedName("weekEnd")
    val weekEnd: String,
    @SerializedName("weekStart")
    val weekStart: String,
)