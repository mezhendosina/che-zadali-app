package com.mezhendosina.sgo.data.requests.login.entities

data class GetDataResponseEntity(
    val lt: String,
    val salt: String,
    val ver: String
)