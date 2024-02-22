package com.mezhendosina.sgo.data.netschoolEsia.entities.mail


import com.google.gson.annotations.SerializedName

data class Mail(
    @SerializedName("items")
    val messages: List<Message>,
    @SerializedName("page")
    val page: Int,
    @SerializedName("pageSize")
    val pageSize: Int,
    @SerializedName("totalItems")
    val totalItems: Int,
    @SerializedName("totalPages")
    val totalPages: Int
)