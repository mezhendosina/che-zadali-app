package com.mezhendosina.sgo.data.requests.notifications

import com.mezhendosina.sgo.data.requests.notifications.entities.NotificationUserEntity
import com.mezhendosina.sgo.data.requests.notifications.entities.UnregisterUserEntity
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NotificationsApi {

    @POST("register_user")
    suspend fun registerUser(
        @Body user: NotificationUserEntity
    ): Response<ResponseBody>

    @POST("unregister_user")
    suspend fun unregisterUser(
        @Body unregisterUserEntity: UnregisterUserEntity
    ): Response<ResponseBody>

    @POST("is_user_exist")
    suspend fun isUserExist(
        @Body body: UnregisterUserEntity
    ): Boolean
}