package com.mezhendosina.sgo.data.requests.sgo.settings.entities

data class MySettingsRequestEntity(
    val email: String,
    val mobilePhone: String,
    val schoolyearId: Int,
    val userId: Int,
    val userSettings: UserSettingsEntity,
    val windowsAccount: Any?
)
