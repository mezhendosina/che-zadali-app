package com.mezhendosina.sgo.data.requests.notifications.entities

data class NotificationUserEntity(
    val id: Int,
    val firebaseToken: String,
    val login: String,
    val password: String,
    val host: String,
    val countryId: Int,
    val sid: Int,
    val provinceId: Int,
    val cityId: Int,
    val sft: Int,
    val schoolId: Int,
    val sendGrades: Int
)
