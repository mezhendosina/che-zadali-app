package com.che.zadali.sgo_app.data.typesId


import com.google.gson.annotations.SerializedName

data class TypesIdItem(
    @SerializedName("abbr")
    val abbr: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("order")
    val order: Int
)