package com.mezhendosina.sgo.data.netschoolEsia.entities.classmeetings


import com.google.gson.annotations.SerializedName
import com.mezhendosina.sgo.data.netschoolEsia.entities.common.Teacher

data class Classmeetings(
    @SerializedName("addEducation")
    val addEducation: Boolean,
    @SerializedName("assignmentId")
    val assignmentId: List<Int>,
    @SerializedName("attachmentsExists")
    val attachmentsExists: Boolean,
    @SerializedName("attendance")
    val attendance: String,
    @SerializedName("classmeetingId")
    val classmeetingId: Int,
    @SerializedName("day")
    val day: String,
    @SerializedName("distanceMeetingId")
    val distanceMeetingId: Any,
    @SerializedName("endTime")
    val endTime: String,
    @SerializedName("extraActivity")
    val extraActivity: Boolean,
    @SerializedName("lessonTheme")
    val lessonTheme: String,
    @SerializedName("order")
    val order: Int,
    @SerializedName("resultsExists")
    val resultsExists: Boolean,
    @SerializedName("roomName")
    val roomName: String,
    @SerializedName("scheduleTimeNumber")
    val scheduleTimeNumber: Int,
    @SerializedName("scheduleTimeRelay")
    val scheduleTimeRelay: Int,
    @SerializedName("startTime")
    val startTime: String,
    @SerializedName("studentId")
    val studentId: Int,
    @SerializedName("subjectGroupId")
    val subjectGroupId: Int,
    @SerializedName("subjectId")
    val subjectId: Int,
    @SerializedName("subjectName")
    val subjectName: String,
    @SerializedName("teachers")
    val teachers: List<Teacher>
)