/*
 * Copyright 2023 Eugene Menshenin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mezhendosina.sgo.data.netschool.repo

import android.content.Context
import android.net.Uri
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.uiEntities.FilterUiEntity
import com.mezhendosina.sgo.app.utils.getFileNameFromUri
import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton
import com.mezhendosina.sgo.data.netschool.api.settings.SettingsSource
import com.mezhendosina.sgo.data.netschool.api.settings.entities.ChangePasswordEntity
import com.mezhendosina.sgo.data.netschool.api.settings.entities.MySettingsRequestEntity
import com.mezhendosina.sgo.data.netschool.api.settings.entities.MySettingsResponseEntity
import com.mezhendosina.sgo.data.netschool.base.BaseRetrofitSource
import com.mezhendosina.sgo.data.requests.notifications.NotificationsSource
import com.mezhendosina.sgo.data.requests.notifications.entities.NotificationUserEntity
import com.mezhendosina.sgo.data.requests.notifications.entities.UnregisterUserEntity
import com.mezhendosina.sgo.di.BaseRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@Module
@InstallIn(ActivityComponent::class)
class SettingsRepository
@Inject constructor(
    private val settingsSource: SettingsSource
) {

    suspend fun getMySettings(): MySettingsResponseEntity {
        return if (Singleton.mySettings.value != null) Singleton.mySettings.value!!
        else settingsSource.getSettings()
    }

    suspend fun loadProfilePhoto(userId: Int, file: File) {
        val photo = settingsSource.getProfilePhoto(userId)
        if (photo != null) {
            file.writeBytes(photo)
        }
    }


    suspend fun changeProfilePhoto(
        context: Context,
        uri: Uri?,
        studentId: Int
    ) {
        val contentResolver = context.contentResolver
        val a = uri?.let { contentResolver.openInputStream(it) }

        val body = a?.readBytes()?.toRequestBody("*/*".toMediaTypeOrNull())
        val fileName = getFileNameFromUri(context, uri)
        if (body != null) {
            val part = MultipartBody.Part.createFormData("file", fileName, body)

            settingsSource.changeProfilePhoto(part, fileName ?: "", studentId)
        }
        withContext(Dispatchers.IO) {
            a?.close()
        }
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

    suspend fun getYears(): List<FilterUiEntity> {
        val res = settingsSource.getYearList()
        return res.map {
            FilterUiEntity(
                it.id,
                (it.name + " год").removePrefix("(*) "),
                !it.name.contains("(*) ")
            )
        }
    }

    suspend fun setYear(id: Int) = settingsSource.setYear(id)
}