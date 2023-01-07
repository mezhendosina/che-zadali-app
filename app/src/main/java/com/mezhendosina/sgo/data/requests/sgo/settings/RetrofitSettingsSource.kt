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

package com.mezhendosina.sgo.data.requests.sgo.settings

import com.mezhendosina.sgo.app.model.settings.SettingsSource
import com.mezhendosina.sgo.data.requests.sgo.base.BaseRetrofitSource
import com.mezhendosina.sgo.data.requests.sgo.base.RetrofitConfig
import com.mezhendosina.sgo.data.requests.sgo.settings.entities.ChangePasswordEntity
import com.mezhendosina.sgo.data.requests.sgo.settings.entities.MySettingsRequestEntity
import com.mezhendosina.sgo.data.requests.sgo.settings.entities.MySettingsResponseEntity
import com.mezhendosina.sgo.data.requests.sgo.settings.entities.YearListResponseEntity
import java.io.File

class RetrofitSettingsSource(config: RetrofitConfig) : BaseRetrofitSource(config), SettingsSource {

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


    override suspend fun getProfilePhoto(at: String, userId: Int): ByteArray? =
        wrapRetrofitExceptions {
            settingsApi.getProfilePhoto(at, userId).body()?.byteStream()?.readBytes()
        }

    override suspend fun changePassword(userId: Int, password: ChangePasswordEntity) =
        wrapRetrofitExceptions {
            settingsApi.changePassword(userId, password)
        }

    override suspend fun changeProfilePhoto(file: File, userId: Int) =
        wrapRetrofitExceptions {
            TODO()
        }

    override suspend fun setYear(id: Int): Unit = wrapRetrofitExceptions {
        settingsApi.setYear(id)
    }
}

