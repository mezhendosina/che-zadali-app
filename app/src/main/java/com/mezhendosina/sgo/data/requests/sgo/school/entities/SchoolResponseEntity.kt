package com.mezhendosina.sgo.data.requests.sgo.school.entities

import com.google.gson.annotations.SerializedName
import com.mezhendosina.sgo.app.model.chooseSchool.SchoolUiEntity

data class SchoolResponseEntity(
    @SerializedName("address")
    val address: Any,
    @SerializedName("cityId")
    val cityId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("inn")
    val inn: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("ogrn")
    val ogrn: String,
    @SerializedName("provinceId")
    val provinceId: Int,
    @SerializedName("shortName")
    val shortName: String
) {
    fun toUiEntity(): SchoolUiEntity = SchoolUiEntity(
        name.replace(shortName, "").replace("(", "").replace(")", ""),
        cityId,
        provinceId,
        shortName,
        id
    )
}