package com.mezhendosina.sgo.data.requests.school.entities

import com.mezhendosina.sgo.app.model.chooseSchool.SchoolUiEntity

data class SchoolResponseEntity(
    val addressString: String,
    val cityDistrictId: Any,
    val cityId: Int,
    val countryId: Int,
    val funcType: Int,
    val id: Int,
    val municipalityDistrictId: Int,
    val name: String,
    val parentCityId: Int,
    val provinceId: Int,
    val stateId: Int
) {
    fun toUiEntity(): SchoolUiEntity = SchoolUiEntity(
        addressString,
        cityId,
        municipalityDistrictId,
        name,
        id
    )
}