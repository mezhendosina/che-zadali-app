package com.mezhendosina.sgo.data.layouts.assignRequest


import com.google.gson.annotations.SerializedName

data class AssignResponse(
    @SerializedName("activityName")
    val activityName: Any,
    @SerializedName("assignmentName")
    val assignmentName: String,
    @SerializedName("attachments")
    val attachments: List<Any>,
    @SerializedName("date")
    val date: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isDeleted")
    val isDeleted: Boolean,
    @SerializedName("problemName")
    val problemName: Any,
    @SerializedName("productId")
    val productId: Any,
    @SerializedName("subjectGroup")
    val subjectGroup: SubjectGroup,
    @SerializedName("teachers")
    val teachers: List<Teacher>,
    @SerializedName("weight")
    val weight: Int
)