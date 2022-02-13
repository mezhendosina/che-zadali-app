package com.che.zadali.sgoapp.data.layout.diary


import com.che.zadali.sgo_app.data.diary.WeekDay
import com.google.gson.annotations.SerializedName

data class Diary(
    @SerializedName("className")
    val className: String,
    @SerializedName("termName")
    val termName: String,
    @SerializedName("weekDays")
    val weekDays: List<WeekDay>,
    @SerializedName("weekEnd")
    val weekEnd: String,
    @SerializedName("weekStart")
    val weekStart: String
)