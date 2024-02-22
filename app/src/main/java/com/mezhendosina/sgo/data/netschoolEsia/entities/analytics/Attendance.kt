package com.mezhendosina.sgo.data.netschoolEsia.entities.analytics


import com.google.gson.annotations.SerializedName

data class Attendance(
    @SerializedName("attendanceMark")
    val attendanceMark: String,
    @SerializedName("classMeetingDate")
    val classMeetingDate: String,
    @SerializedName("classMeetingId")
    val classMeetingId: Int
)