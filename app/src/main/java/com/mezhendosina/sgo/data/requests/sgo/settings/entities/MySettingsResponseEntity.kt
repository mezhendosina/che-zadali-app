package com.mezhendosina.sgo.data.requests.sgo.settings.entities

data class MySettingsResponseEntity(
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
    val userSettings: UserSettingsEntity,
    val windowsAccount: Any
) {
    fun toRequestEntity(): MySettingsRequestEntity = MySettingsRequestEntity(
        email, mobilePhone, schoolyearId, userId, userSettings, windowsAccount
    )
}