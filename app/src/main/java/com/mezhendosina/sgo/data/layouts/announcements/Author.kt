package com.mezhendosina.sgo.data.layouts.announcements


import com.google.gson.annotations.SerializedName

data class Author(
    @SerializedName("fio")
    val fio: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("nickName")
    val nickName: String
)