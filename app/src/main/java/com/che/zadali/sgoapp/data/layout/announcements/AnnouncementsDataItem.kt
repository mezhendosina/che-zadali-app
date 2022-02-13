package com.che.zadali.sgo_app.data.announcements


import com.google.gson.annotations.SerializedName

data class AnnouncementsDataItem(
    @SerializedName("attachments")
    val attachments: List<Attachment>,
    @SerializedName("author")
    val author: Author,
    @SerializedName("deleteDate")
    val deleteDate: Any,
    @SerializedName("description")
    val description: String,
    @SerializedName("em")
    val em: Any,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("postDate")
    val postDate: String,
    @SerializedName("recipientInfo")
    val recipientInfo: Any
)