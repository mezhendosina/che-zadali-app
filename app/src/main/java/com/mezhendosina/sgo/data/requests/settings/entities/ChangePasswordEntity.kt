package com.mezhendosina.sgo.data.requests.settings.entities

data class ChangePasswordEntity(
    val oldPassword: String,
    val password: String
)