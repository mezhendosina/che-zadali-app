package com.mezhendosina.sgo.data.netschoolEsia.entities.login

import com.google.gson.annotations.SerializedName

data class GetUsersResponseItem(
    @SerializedName("serverId")
    val serverId: String,
    @SerializedName("userId")
    val userId: String,
)
