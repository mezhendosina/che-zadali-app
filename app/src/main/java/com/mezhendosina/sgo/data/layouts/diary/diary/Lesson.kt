package com.mezhendosina.sgo.data.layouts.diary.diary


import com.google.gson.annotations.SerializedName

data class Lesson(
    @SerializedName("assignments")
    val assignments: List<Assignment>?,
    @SerializedName("classmeetingId")
    val classmeetingId: Int,
    @SerializedName("day")
    val day: String,
    @SerializedName("endTime")
    val endTime: String,
    @SerializedName("isEaLesson")
    val isEaLesson: Boolean,
    @SerializedName("number")
    val number: Int,
    @SerializedName("relay")
    val relay: Int,
    @SerializedName("room")
    val room: Any,
    @SerializedName("startTime")
    val startTime: String,
    @SerializedName("subjectName")
    val subjectName: String
)