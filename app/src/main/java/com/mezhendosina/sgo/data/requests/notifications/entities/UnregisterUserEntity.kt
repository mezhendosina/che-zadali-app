package com.mezhendosina.sgo.data.requests.notifications.entities

data class UnregisterUserEntity(
    val userId: Int,
    val firebaseToken: String
)