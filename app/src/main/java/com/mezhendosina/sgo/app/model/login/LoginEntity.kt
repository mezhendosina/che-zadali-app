package com.mezhendosina.sgo.app.model.login

data class LoginEntity(
    val schoolId: Int,
    val login: String,
    val password: String,
    val lt: String,
    val salt: String,
    val ver: String
)
