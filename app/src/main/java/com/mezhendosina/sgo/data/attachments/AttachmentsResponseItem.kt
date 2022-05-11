package com.mezhendosina.sgo.data.attachments


import com.google.gson.annotations.SerializedName

data class AttachmentsResponseItem(
    @SerializedName("answerFiles")
    val answerFiles: List<Any>,
    @SerializedName("assignmentId")
    val assignmentId: Int,
    @SerializedName("attachments")
    val attachments: List<Attachment>
)