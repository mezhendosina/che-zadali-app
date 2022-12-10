package com.mezhendosina.sgo.data.requests.sgo.login.entities

data class GetDataResponseEntity(
    val lt: String,
    val salt: String,
    val ver: String
)