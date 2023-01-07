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

import com.mezhendosina.sgo.data.requests.sgo.settings.entities.ChangePasswordEntity
import com.mezhendosina.sgo.data.requests.sgo.settings.entities.MySettingsRequestEntity
import com.mezhendosina.sgo.data.requests.sgo.settings.entities.MySettingsResponseEntity
import com.mezhendosina.sgo.data.requests.sgo.settings.entities.YearListResponseEntity
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface SettingsApi {

    @GET("webapi/mysettings/yearlist")
    suspend fun getYearList(): List<YearListResponseEntity>

    @GET("webapi/mysettings")
    suspend fun getSettings(): MySettingsResponseEntity

    @POST("webapi/mysettings/")
    suspend fun sendSettings(
        @Body mySettingsRequestEntity: MySettingsRequestEntity
    ): Response<Unit>

    @GET("webapi/users/photo")
    suspend fun getProfilePhoto(
        @Query("at") at: String,
        @Query("userId") userId: Int,
    ): Response<ResponseBody>

    @POST("webapi/users/{userId}/password")
    suspend fun changePassword(
        @Path("userId") userId: Int,
        @Body password: ChangePasswordEntity,
    )

    @Multipart
    @POST("asp/SetupSchool/PhotoSave.asp")
    suspend fun changeProfilePhoto(
        @Part("fileName") fileName: String,
        @Part("userId") userId: Int,
        @Part("file") file: MultipartBody.Part
    )

    @POST("webapi/context/year")
    suspend fun setYear(
        @Body year: Int
    ): Response<ResponseBody>

}
