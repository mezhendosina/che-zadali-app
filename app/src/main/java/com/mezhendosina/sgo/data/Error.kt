package com.mezhendosina.sgo.data

import com.google.gson.annotations.SerializedName

data class Error(
    @SerializedName("message")
    val message: String
)
