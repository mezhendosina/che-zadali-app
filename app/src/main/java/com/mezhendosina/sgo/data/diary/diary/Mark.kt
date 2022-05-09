package com.mezhendosina.sgo.data.diary.diary


import com.google.gson.annotations.SerializedName

data class Mark(
    @SerializedName("assignmentId")
    val assignmentId: Int,
    @SerializedName("dutyMark")
    val dutyMark: Boolean,
    @SerializedName("mark")
    val mark: Int,
    @SerializedName("resultScore")
    val resultScore: Any,
    @SerializedName("studentId")
    val studentId: Int
)