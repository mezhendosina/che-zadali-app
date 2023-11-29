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

package com.mezhendosina.sgo.data.netschool.api.settings

import com.mezhendosina.sgo.data.netschool.api.settings.entities.ChangePasswordEntity
import com.mezhendosina.sgo.data.netschool.api.settings.entities.MySettingsRequestEntity
import com.mezhendosina.sgo.data.netschool.api.settings.entities.MySettingsResponseEntity
import com.mezhendosina.sgo.data.netschool.api.settings.entities.YearListResponseEntity
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response

interface SettingsSource {

    suspend fun getYearList(): List<YearListResponseEntity>

    suspend fun getSettings(): MySettingsResponseEntity

    suspend fun sendSettings(mySettingsRequestEntity: MySettingsRequestEntity)

    suspend fun getProfilePhoto(userId: Int): ByteArray?

    suspend fun changePassword(userId: Int, password: ChangePasswordEntity)

    suspend fun changeProfilePhoto(file: MultipartBody.Part, fileName: String, userId: Int)

    suspend fun setYear(id: Int): Response<ResponseBody>
}