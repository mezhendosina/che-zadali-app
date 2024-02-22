package com.mezhendosina.sgo.data.netschoolEsia.entities.mail


import com.google.gson.annotations.SerializedName
import com.mezhendosina.sgo.data.netschoolEsia.entities.common.Author

data class Message(
    @SerializedName("author")
    val author: Author,
    @SerializedName("fileAttachments")
    val fileAttachments: Any,
    @SerializedName("id")
    val id: Int,
    @SerializedName("mailBox")
    val mailBox: String,
    @SerializedName("noReply")
    val noReply: Boolean,
    @SerializedName("notify")
    val notify: Boolean,
    @SerializedName("read")
    val read: Boolean,
    @SerializedName("sent")
    val sent: String,
    @SerializedName("subject")
    val subject: String,
    @SerializedName("text")
    val text: String
)