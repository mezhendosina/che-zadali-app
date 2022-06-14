package com.mezhendosina.sgo.data.layouts.attachments


import com.google.gson.annotations.SerializedName

data class Attachment(
    @SerializedName("description")
    val description: Any,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: Any,
    @SerializedName("originalFileName")
    val originalFileName: String
)