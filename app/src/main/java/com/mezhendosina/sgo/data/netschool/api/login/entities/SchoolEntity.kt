package com.mezhendosina.sgo.app.netschool.api.login.entities

import com.google.gson.annotations.SerializedName
import com.mezhendosina.sgo.app.uiEntities.SchoolUiEntity

data class SchoolEntity(
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
    fun toUiEntity() = SchoolUiEntity(
        id,
        shortName,
        name.replace(shortName, "").replace("(", "").replace(")", "")
    )
}

