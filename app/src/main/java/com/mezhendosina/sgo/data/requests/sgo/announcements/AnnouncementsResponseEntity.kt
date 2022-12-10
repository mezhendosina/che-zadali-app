package com.mezhendosina.sgo.data.requests.sgo.announcements


import com.google.gson.annotations.SerializedName
import com.mezhendosina.sgo.data.requests.sgo.homework.entities.Attachment

data class AnnouncementsResponseEntity(
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


data class Author(
    @SerializedName("fio")
    val fio: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("nickName")
    val nickName: String
)

data class Em(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)