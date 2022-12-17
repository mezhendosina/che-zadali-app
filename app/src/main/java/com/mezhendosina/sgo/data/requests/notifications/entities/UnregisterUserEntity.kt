package com.mezhendosina.sgo.data.requests.notifications.entities

data class UnregisterUserEntity(
    val id: Int,
    val firebaseToken: String
)