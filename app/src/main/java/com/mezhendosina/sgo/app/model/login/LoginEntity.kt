package com.mezhendosina.sgo.app.model.login

data class LoginEntity(
    val countryId: Int,
    val stateId: Int,
    val provinceId: Int,
    val cityId: Int,
    val schoolType: Int,
    val schoolId: Int,
    val login: String,
    val password: String,
    val lt: String,
    val salt: String,
    val ver: String
)
