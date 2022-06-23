package com.mezhendosina.sgo.data.layouts.mySettingsRequest

data class UserSettings(
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