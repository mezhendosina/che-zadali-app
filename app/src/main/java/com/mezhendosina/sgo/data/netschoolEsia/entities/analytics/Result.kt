package com.mezhendosina.sgo.data.netschoolEsia.entities.analytics


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("assignmentId")
    val assignmentId: Int,
    @SerializedName("assignmentTypeAbbr")
    val assignmentTypeAbbr: String,
    @SerializedName("assignmentTypeId")
    val assignmentTypeId: Int,
    @SerializedName("assignmentTypeName")
    val assignmentTypeName: String,
    @SerializedName("classMeetingDate")
    val classMeetingDate: String,
    @SerializedName("classMeetingId")
    val classMeetingId: Int,
    @SerializedName("comment")
    val comment: Any,
    @SerializedName("date")
    val date: String,
    @SerializedName("duty")
    val duty: Boolean,
    @SerializedName("result")
    val result: Double,
    @SerializedName("weight")
    val weight: Int
)