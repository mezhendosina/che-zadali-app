package com.che.zadali.sgo_app.data.diary


import com.google.gson.annotations.SerializedName

data class WeekDay(
    @SerializedName("date")
    val date: String,
    @SerializedName("lessons")
    val lessons: List<Lesson>
)