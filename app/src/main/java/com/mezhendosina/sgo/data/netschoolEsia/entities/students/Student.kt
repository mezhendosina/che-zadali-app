package com.mezhendosina.sgo.data.netschoolEsia.entities.students


import com.google.gson.annotations.SerializedName

data class Student(
    @SerializedName("name")
    val name: String,
    @SerializedName("shortName")
    val shortName: String,
    @SerializedName("studentId")
    val studentId: Int
)