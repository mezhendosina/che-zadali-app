package com.mezhendosina.sgo.data.layouts.mySettings

data class MySettings(
    val birthDate: String,
    val email: String,
    val existsPhoto: Boolean,
    val firstName: String,
    val lastName: String,
    val loginName: String,
    val middleName: String,
    val mobilePhone: String,
    val preferedCom: String,
    val roles: List<String>,
    val schoolyearId: Int,
    val userId: Int,
    val userSettings: UserSettings,
    val windowsAccount: Any
)