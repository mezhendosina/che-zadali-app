package com.mezhendosina.sgo.data.netschoolEsia.entities.organizations


import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("municipality")
    val municipality: Municipality,
    @SerializedName("name")
    val name: String,
    @SerializedName("orgType")
    val orgType: String
)