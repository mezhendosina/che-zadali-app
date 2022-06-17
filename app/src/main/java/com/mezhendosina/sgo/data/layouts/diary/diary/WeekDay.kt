package com.mezhendosina.sgo.data.layouts.diary.diary


import com.google.gson.annotations.SerializedName

data class WeekDay(
    @SerializedName("date")
    val date: String,
    @SerializedName("lessons")
    val lessons: List<Lesson>
)