package com.mezhendosina.sgo.app.model.chooseSchool


import com.google.gson.annotations.SerializedName

data class SchoolUiEntity(
    @SerializedName("city")
    val city: String,
    @SerializedName("city_id")
    val cityId: Int,
    @SerializedName("province_id")
    val provinceId: Int,
    @SerializedName("school")
    val school: String,
    @SerializedName("SchoolId")
    val schoolId: Int
)