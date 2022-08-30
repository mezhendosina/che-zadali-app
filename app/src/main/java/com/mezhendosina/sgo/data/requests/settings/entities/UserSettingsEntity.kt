package com.mezhendosina.sgo.data.requests.settings.entities

data class UserSettingsEntity(
    val showMobilePhone: Boolean,
    val defaultDesktop: Int,
    val favoriteReports: List<Any>,
    val language: String,
    val passwordExpired: Int,
    val recoveryAnswer: String,
    val recoveryQuestion: String,
    val showNetSchoolApp: Boolean,
    val theme: Int,
    val userId: Int
)