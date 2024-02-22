package com.mezhendosina.sgo.data.netschoolEsia.entities.organizations


import com.google.gson.annotations.SerializedName

data class Organization(
    @SerializedName("items")
    val items: List<Item>,
    @SerializedName("page")
    val page: Int,
    @SerializedName("pageSize")
    val pageSize: Int,
    @SerializedName("totalItems")
    val totalItems: Int,
    @SerializedName("totalPages")
    val totalPages: Int
)