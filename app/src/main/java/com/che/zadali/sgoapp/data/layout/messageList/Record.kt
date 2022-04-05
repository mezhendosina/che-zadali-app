package com.che.zadali.sgoapp.data.layout.messageList


import com.google.gson.annotations.SerializedName

data class Record(
    @SerializedName("FromEOName")
    val fromEOName: String,
    @SerializedName("FromName")
    val fromName: String,
    @SerializedName("MessageId")
    val messageId: Int,
    @SerializedName("Read")
    val read: String,
    @SerializedName("Sent")
    val sent: String,
    @SerializedName("SentTo")
    val sentTo: String,
    @SerializedName("Subj")
    val subj: String
)