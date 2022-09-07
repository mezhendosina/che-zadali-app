package com.mezhendosina.sgo.data.requests.other.entities.schools


import com.google.gson.annotations.SerializedName

data class SchoolItem(
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