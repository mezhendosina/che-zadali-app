package com.mezhendosina.sgo.data.netschoolEsia.entities.announcements


import com.google.gson.annotations.SerializedName

data class AttachmentEntity(
    @SerializedName("attachmentId")
    val attachmentId: Int,
    @SerializedName("description")
    val description: Any,
    @SerializedName("fileName")
    val fileName: String
)