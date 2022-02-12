package com.che.zadali.sgo_app.data.diary


import com.google.gson.annotations.SerializedName

data class Mark(
    @SerializedName("assignmentId")
    val assignmentId: Int,
    @SerializedName("dutyMark")
    val dutyMark: Boolean,
    @SerializedName("mark")
    val mark: Int?,
    @SerializedName("resultScore")
    val resultScore: Any?,
    @SerializedName("studentId")
    val studentId: Int
)