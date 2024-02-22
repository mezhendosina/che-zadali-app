package com.mezhendosina.sgo.data.netschoolEsia.entities.analytics


import com.google.gson.annotations.SerializedName
import com.mezhendosina.sgo.data.netschoolEsia.entities.common.Teacher

data class SubjectPerformance(
    @SerializedName("attendance")
    val attendance: List<Attendance>,
    @SerializedName("averageMark")
    val averageMark: Double,
    @SerializedName("classAverageMark")
    val classAverageMark: Double,
    @SerializedName("classmeetingsStats")
    val classmeetingsStats: ClassmeetingsStats,
    @SerializedName("markStats")
    val markStats: List<MarkStat>,
    @SerializedName("maxMark")
    val maxMark: Int,
    @SerializedName("results")
    val results: List<Result>,
    @SerializedName("subject")
    val subject: Subject,
    @SerializedName("teachers")
    val teachers: List<Teacher>,
    @SerializedName("term")
    val term: Term
)