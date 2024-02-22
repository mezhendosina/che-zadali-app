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
import com.mezhendosina.sgo.data.netschool.api.settings.entities.SendPhotoRequestEntity
import com.mezhendosina.sgo.data.netschool.api.settings.entities.YearListResponseEntity
import com.mezhendosina.sgo.data.netschool.base.BaseRetrofitSource
import com.mezhendosina.sgo.data.netschool.base.RetrofitConfig
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitSettingsSource
    @Inject
    constructor(config: RetrofitConfig) :
    BaseRetrofitSource(config), SettingsSource {
        private val settingsApi = retrofit.create(SettingsApi::class.java)

        override suspend fun getYearList(): List<YearListResponseEntity> =
            wrapRetrofitExceptions {
                settingsApi.getYearList()
            }

        override suspend fun getSettings(): MySettingsResponseEntity =
            wrapRetrofitExceptions {
                settingsApi.getSettings()
            }

        override suspend fun sendSettings(mySettingsRequestEntity: MySettingsRequestEntity): Unit =
            wrapRetrofitExceptions {
                settingsApi.sendSettings(mySettingsRequestEntity)
            }

        override suspend fun getProfilePhoto(userId: Int): ByteArray? =
            wrapRetrofitExceptions {
                settingsApi.getProfilePhoto(com.mezhendosina.sgo.Singleton.at, userId).body()
                    ?.byteStream()?.readBytes()
            }

        override suspend fun changePassword(
            userId: Int,
            password: ChangePasswordEntity,
        ) = wrapRetrofitExceptions {
            settingsApi.changePassword(userId, password)
        }

        override suspend fun changeProfilePhoto(
            file: MultipartBody.Part,
            fileName: String,
            userId: Int,
        ) = wrapRetrofitExceptions {
            settingsApi.changeProfilePhoto(file, SendPhotoRequestEntity(fileName, userId))
        }

        override suspend fun setYear(id: Int): Response<ResponseBody> =
            wrapRetrofitExceptions {
                settingsApi.setYear(id)
            }
    }
