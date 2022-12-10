package com.mezhendosina.sgo.app.model.settings

import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.data.requests.sgo.settings.entities.ChangePasswordEntity
import com.mezhendosina.sgo.data.requests.sgo.settings.entities.MySettingsRequestEntity
import com.mezhendosina.sgo.data.requests.sgo.settings.entities.MySettingsResponseEntity
import com.mezhendosina.sgo.data.requests.sgo.settings.entities.YearListResponseEntity
import java.io.File

class SettingsRepository(private val settingsSource: SettingsSource) {

    suspend fun getMySettings(): MySettingsResponseEntity {
        return if (Singleton.mySettings != null) Singleton.mySettings!!
        else settingsSource.getSettings()
    }

    suspend fun loadProfilePhoto(userId: Int, file: File, isExist: Boolean) {
        if (isExist) {
            val photo = settingsSource.getProfilePhoto(Singleton.at, userId)
            if (photo != null) {
                file.writeBytes(photo)
            }
        }
    }

    suspend fun sendSettings(mySettingsRequestEntity: MySettingsRequestEntity) {
        settingsSource.sendSettings(mySettingsRequestEntity)
    }

    suspend fun changePhoto(file: File, userId: Int) {
        settingsSource.changeProfilePhoto(file, userId)
    }

    suspend fun changePassword(userId: Int, password: ChangePasswordEntity) {
        settingsSource.changePassword(userId, password)
    }

    suspend fun getYears(): List<YearListResponseEntity> =
        settingsSource.getYearList()

}