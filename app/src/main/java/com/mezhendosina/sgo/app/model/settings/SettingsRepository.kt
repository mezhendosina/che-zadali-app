package com.mezhendosina.sgo.app.model.settings

import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.data.requests.notifications.NotificationsSource
import com.mezhendosina.sgo.data.requests.notifications.entities.NotificationUserEntity
import com.mezhendosina.sgo.data.requests.notifications.entities.UnregisterUserEntity
import com.mezhendosina.sgo.data.requests.sgo.settings.entities.ChangePasswordEntity
import com.mezhendosina.sgo.data.requests.sgo.settings.entities.MySettingsRequestEntity
import com.mezhendosina.sgo.data.requests.sgo.settings.entities.MySettingsResponseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class SettingsRepository(private val settingsSource: SettingsSource) {
    private val notificationsSource = NotificationsSource()

    suspend fun getMySettings(): MySettingsResponseEntity {
        return if (Singleton.mySettings.value != null) Singleton.mySettings.value!!
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

    suspend fun registerGradesNotifications(userEntity: NotificationUserEntity) {
        notificationsSource.notificationsSource.registerUser(userEntity)
    }

    suspend fun isGradesNotifyUserExist(userId: Int, firebaseToken: String): Boolean =
        notificationsSource.notificationsSource.isUserExist(
            UnregisterUserEntity(
                userId,
                firebaseToken
            )
        )


    suspend fun unregisterGradesNotifications(userId: Int, token: String) {
        notificationsSource.notificationsSource.unregisterUser(
            UnregisterUserEntity(
                userId,
                token
            )
        )
    }

    suspend fun sendSettings(mySettingsRequestEntity: MySettingsRequestEntity) {
        settingsSource.sendSettings(mySettingsRequestEntity)
        withContext(Dispatchers.Main) {
            Singleton.mySettings.value = settingsSource.getSettings()
        }
    }

    suspend fun changePassword(userId: Int, password: ChangePasswordEntity) {
        settingsSource.changePassword(userId, password)
    }
}