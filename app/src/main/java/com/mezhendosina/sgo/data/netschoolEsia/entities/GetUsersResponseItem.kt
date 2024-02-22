package com.mezhendosina.sgo.data.netschoolEsia.entities


import com.google.gson.annotations.SerializedName

data class GetUsersResponseItem(
    @SerializedName("serverId")
    val serverId: String,
    @SerializedName("userId")
    val userId: String
)