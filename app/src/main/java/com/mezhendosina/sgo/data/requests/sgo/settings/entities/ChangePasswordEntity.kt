package com.mezhendosina.sgo.data.requests.sgo.settings.entities

data class ChangePasswordEntity(
    val oldPassword: String,
    val password: String
)