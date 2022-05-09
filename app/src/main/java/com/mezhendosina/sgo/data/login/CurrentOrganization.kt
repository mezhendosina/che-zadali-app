package com.mezhendosina.sgo.data.login


import com.google.gson.annotations.SerializedName

data class CurrentOrganization(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)