package com.mezhendosina.sgo.data.diary.init


import com.google.gson.annotations.SerializedName

data class Student(
    @SerializedName("classId")
    val classId: Int,
    @SerializedName("className")
    val className: Any,
    @SerializedName("iupGrade")
    val iupGrade: Int,
    @SerializedName("nickName")
    val nickName: String,
    @SerializedName("studentId")
    val studentId: Int
)