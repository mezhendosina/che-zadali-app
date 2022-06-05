package com.mezhendosina.sgo.data


import com.google.gson.annotations.SerializedName

data class NegotiateResponse(
    @SerializedName("ConnectionId")
    val connectionId: String,
    @SerializedName("ConnectionTimeout")
    val connectionTimeout: Double,
    @SerializedName("ConnectionToken")
    val connectionToken: String,
    @SerializedName("DisconnectTimeout")
    val disconnectTimeout: Double,
    @SerializedName("KeepAliveTimeout")
    val keepAliveTimeout: Double,
    @SerializedName("LongPollDelay")
    val longPollDelay: Double,
    @SerializedName("ProtocolVersion")
    val protocolVersion: String,
    @SerializedName("TransportConnectTimeout")
    val transportConnectTimeout: Double,
    @SerializedName("TryWebSockets")
    val tryWebSockets: Boolean,
    @SerializedName("Url")
    val url: String
)