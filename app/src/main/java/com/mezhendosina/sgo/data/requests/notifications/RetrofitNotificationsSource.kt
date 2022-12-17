package com.mezhendosina.sgo.data.requests.notifications

import com.mezhendosina.sgo.data.requests.notifications.entities.NotificationUserEntity
import com.mezhendosina.sgo.data.requests.notifications.entities.UnregisterUserEntity
import com.mezhendosina.sgo.data.requests.sgo.base.BaseRetrofitSource
import com.mezhendosina.sgo.data.requests.sgo.base.RetrofitConfig
import okhttp3.ResponseBody
import retrofit2.Response

class RetrofitNotificationsSource(
    config: RetrofitConfig
) : BaseRetrofitSource(config), NotificationsApi {

    private val notificationsApi = retrofit.create(NotificationsApi::class.java)

    override suspend fun registerUser(user: NotificationUserEntity): Response<ResponseBody> =
        wrapRetrofitExceptions {
            notificationsApi.registerUser(user)
        }

    override suspend fun unregisterUser(unregisterUserEntity: UnregisterUserEntity): Response<ResponseBody> =
        wrapRetrofitExceptions {
            notificationsApi.unregisterUser(unregisterUserEntity)
        }

    override suspend fun isUserExist(body: UnregisterUserEntity): Boolean =
        wrapRetrofitExceptions {
            notificationsApi.isUserExist(body)
        }
}