package com.mezhendosina.sgo.data.netschoolEsia.entities.announcements


import com.google.gson.annotations.SerializedName
import com.mezhendosina.sgo.data.netschoolEsia.entities.common.Author

data class Announcement(
    @SerializedName("announcementId")
    val announcementId: Int,
    @SerializedName("attachments")
    val attachmentEntities: List<AttachmentEntity>,
    @SerializedName("author")
    val author: Author,
    @SerializedName("description")
    val description: String,
    @SerializedName("organizations")
    val organizations: List<Organization>,
    @SerializedName("postDate")
    val postDate: String,
    @SerializedName("recipients")
    val recipients: List<String>,
    @SerializedName("title")
    val title: String
)