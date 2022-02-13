package com.che.zadali.sgoapp.data.layout.diary


import com.che.zadali.sgo_app.data.diary.Mark
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
    @SerializedName("typeId")
    val typeId: Int,
    @SerializedName("weight")
    val weight: Int
)