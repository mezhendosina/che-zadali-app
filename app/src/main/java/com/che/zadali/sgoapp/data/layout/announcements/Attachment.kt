package com.che.zadali.sgoapp.data.layout.announcements


import com.google.gson.annotations.SerializedName

data class Attachment(
    @SerializedName("description")
    val description: Any,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("originalFileName")
    val originalFileName: String
)