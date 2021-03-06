package com.mezhendosina.sgo.data.layouts.announcements


import com.google.gson.annotations.SerializedName
import com.mezhendosina.sgo.data.layouts.attachments.Attachment

data class AnnouncementsResponseItem(
    @SerializedName("attachments")
    val attachments: List<Attachment>,
    @SerializedName("author")
    val author: Author,
    @SerializedName("deleteDate")
    val deleteDate: Any,
    @SerializedName("description")
    val description: String,
    @SerializedName("em")
    val em: Em,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("postDate")
    val postDate: String,
    @SerializedName("recipientInfo")
    val recipientInfo: Any
)