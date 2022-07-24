package com.mezhendosina.sgo.data.layouts.diary.diary


import com.google.gson.annotations.SerializedName

data class Assignment(
    @SerializedName("assignmentName")
    val assignmentName: String,
    @SerializedName("classMeetingId")
    val classMeetingId: Int,
    @SerializedName("dueDate")
    val dueDate: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("mark")
    val mark: Mark?,
    @SerializedName("textAnswer")
    val textAnswer: TextAnswer?,
    @SerializedName("typeId")
    val typeId: Int,
    @SerializedName("weight")
    val weight: Int
)