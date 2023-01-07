package com.mezhendosina.sgo.data.requests.sgo.settings.entities

data class MySettingsRequestEntity(
    val email: String?,
    val mobilePhone: String?,
    val schoolyearId: Int,
    val userId: Int,
    val userSettings: UserSettingsEntity,
    val windowsAccount: Any?
) {
    fun changeEmail(email: String): MySettingsRequestEntity = MySettingsRequestEntity(
        email,
        this.mobilePhone,
        this.schoolyearId,
        this.userId,
        this.userSettings,
        this.windowsAccount
    )

    fun changeMobilePhone(phoneNumber: String): MySettingsRequestEntity = MySettingsRequestEntity(
        this.email,
        phoneNumber.replace("[^\\d]".toRegex(), ""),
        this.schoolyearId,
        this.userId,
        this.userSettings,
        this.windowsAccount
    )
}
