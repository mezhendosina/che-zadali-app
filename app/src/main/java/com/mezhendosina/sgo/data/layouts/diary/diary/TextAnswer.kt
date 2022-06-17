package com.mezhendosina.sgo.data.layouts.diary.diary


import com.google.gson.annotations.SerializedName

data class TextAnswer(
    @SerializedName("answer")
    val answer: String,
    @SerializedName("answerDate")
    val answerDate: String
)