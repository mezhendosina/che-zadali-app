package com.mezhendosina.sgo.data.announcements


import com.google.gson.annotations.SerializedName

data class Em(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)