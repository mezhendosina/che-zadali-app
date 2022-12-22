package com.mezhendosina.sgo.data.requests.notifications.entities

data class NotificationUserEntity(
    val id: Int,
    val firebaseToken: String,
    val login: String,
    val password: String,
    val host: String,
    val schoolId: Int,
    val sendGrades: Boolean
)
