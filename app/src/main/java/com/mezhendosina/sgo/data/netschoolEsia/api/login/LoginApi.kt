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

package com.mezhendosina.sgo.data.netschoolEsia.api.login

import com.mezhendosina.sgo.data.netschool.api.login.entities.accountInfo.AccountInfoResponseEntity
import com.mezhendosina.sgo.data.requests.sgo.login.entities.LoginResponseEntity
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginApi {
    @GET("webapi/sso/esia/crosslogin?esia_permissions=1&esia_role=1")
    suspend fun crossLogin(
        @Header("Sec-Fetch-Mode") secFetchMode: String = "navigate",
        @Header("Sec-Fetch-Dest") secFetchDest: String = "document",
        @Header("Sec-Fetch-User") secFetchUser: String = "?1",
        @Header("Upgrade-Insecure-Requests") upgradeInsecureRequests: Int = 1,
    ): Response<ResponseBody>

    @GET("webapi/sso/esia/account-info")
    suspend fun getAccountInfo(
        @Query("loginState") loginState: String,
    ): AccountInfoResponseEntity

    @POST("webapi/auth/login")
    @FormUrlEncoded
    suspend fun gosuslugiLogin(
        @Field("loginState") loginState: String,
        @Field("lscope") lscope: String,
        @Field("idp") idp: String = "esia",
        @Field("loginType") loginType: String = "8",
    ): LoginResponseEntity

    @POST("webapi/auth/login-state")
    @FormUrlEncoded
    suspend fun loginState(
        @Field("mobile") mobile: Int = 1,
    ): String

    @GET("webapi/sso/sia/account-info")
    suspend fun accountInfo(
        @Field("loginState") loginState: String,
    )
}
