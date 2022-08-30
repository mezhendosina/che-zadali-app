package com.mezhendosina.sgo.app.model.settings

import com.mezhendosina.sgo.data.requests.settings.entities.ChangePasswordEntity
import com.mezhendosina.sgo.data.requests.settings.entities.MySettingsRequestEntity
import com.mezhendosina.sgo.data.requests.settings.entities.MySettingsResponseEntity
import com.mezhendosina.sgo.data.requests.settings.entities.YearListResponseEntity
import retrofit2.Response
import java.io.File

interface SettingsSource {

    suspend fun getYearList(): List<YearListResponseEntity>

    suspend fun getSettings(): MySettingsResponseEntity

    suspend fun sendSettings(mySettingsRequestEntity: MySettingsRequestEntity)

    suspend fun getProfilePhoto(at: String, userId: Int): ByteArray?

    suspend fun changePassword(userId: Int, password: ChangePasswordEntity)

    suspend fun changeProfilePhoto(file: File, userId: Int)

}