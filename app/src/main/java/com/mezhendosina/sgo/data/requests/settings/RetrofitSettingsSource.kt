package com.mezhendosina.sgo.data.requests.settings

import com.mezhendosina.sgo.app.model.settings.SettingsSource
import com.mezhendosina.sgo.data.requests.base.BaseRetrofitSource
import com.mezhendosina.sgo.data.requests.base.RetrofitConfig
import com.mezhendosina.sgo.data.requests.settings.entities.ChangePasswordEntity
import com.mezhendosina.sgo.data.requests.settings.entities.MySettingsRequestEntity
import com.mezhendosina.sgo.data.requests.settings.entities.MySettingsResponseEntity
import com.mezhendosina.sgo.data.requests.settings.entities.YearListResponseEntity
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
}

