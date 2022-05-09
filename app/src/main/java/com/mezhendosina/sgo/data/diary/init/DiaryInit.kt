package com.mezhendosina.sgo.data.diary.init


import com.google.gson.annotations.SerializedName

data class DiaryInit(
    @SerializedName("currentStudentId")
    val currentStudentId: Int,
    @SerializedName("externalUrl")
    val externalUrl: String,
    @SerializedName("maxMark")
    val maxMark: Int,
    @SerializedName("newDiskToken")
    val newDiskToken: String,
    @SerializedName("newDiskWasRequest")
    val newDiskWasRequest: Boolean,
    @SerializedName("students")
    val students: List<Student>,
    @SerializedName("ttsuRl")
    val ttsuRl: String,
    @SerializedName("weekStart")
    val weekStart: String,
    @SerializedName("weight")
    val weight: Boolean,
    @SerializedName("withLaAssigns")
    val withLaAssigns: Boolean,
    @SerializedName("yaClass")
    val yaClass: Boolean,
    @SerializedName("yaClassAuthUrl")
    val yaClassAuthUrl: Any
)