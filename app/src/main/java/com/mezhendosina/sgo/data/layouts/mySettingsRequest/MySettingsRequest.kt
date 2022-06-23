package com.mezhendosina.sgo.data.layouts.mySettingsRequest

data class MySettingsRequest(
    val email: String,
    val mobilePhone: String,
    val schoolyearId: Int,
    val userId: Int,
    val userSettings: UserSettings,
    val windowsAccount: Any?
)