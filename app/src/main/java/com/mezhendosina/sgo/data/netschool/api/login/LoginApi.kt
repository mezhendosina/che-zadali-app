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

package com.mezhendosina.sgo.data.netschool.api.login

import com.mezhendosina.sgo.app.netschool.api.login.entities.SchoolEntity
import com.mezhendosina.sgo.data.netschool.api.login.entities.StudentResponseEntity
import com.mezhendosina.sgo.data.netschool.api.login.entities.accountInfo.AccountInfoResponseEntity
import com.mezhendosina.sgo.data.requests.sgo.login.entities.GetDataResponseEntity
import com.mezhendosina.sgo.data.requests.sgo.login.entities.LoginResponseEntity
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginApi {
    @GET("webapi/schools/search")
    suspend fun getSchools(
        @Query("name") schoolName: String
    ): List<SchoolEntity>

    @GET("webapi/logindata")
    suspend fun loginData()

    @POST("webapi/auth/getdata")
    suspend fun getData(): GetDataResponseEntity

    @POST("webapi/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("LoginType") loginType: Int,
        @Field("scid") scid: Int,
        @Field("UN") UN: String,
        @Field("PW") PW: String,
        @Field("lt") lt: String,
        @Field("pw2") pw2: String,
        @Field("ver") ver: String
    ): LoginResponseEntity

    @GET("webapi/context/students")
    suspend fun getStudents(): Response<List<StudentResponseEntity>>

    @POST("asp/logout.asp")
    @FormUrlEncoded
    suspend fun logout(@Field("at") at: String)

    @GET("webapi/sso/esia/crosslogin?esia_permissions=1&esia_role=1")
    suspend fun crossLogin(
        @Header("Sec-Fetch-Mode") secFetchMode: String = "navigate",
        @Header("Sec-Fetch-Dest") secFetchDest: String = "document",
        @Header("Sec-Fetch-User") secFetchUser: String = "?1",
        @Header("Upgrade-Insecure-Requests") upgradeInsecureRequests: Int = 1
    ): Response<ResponseBody>

    @GET("webapi/sso/esia/account-info")
    suspend fun getAccountInfo(
        @Query("loginState") loginState: String
    ): AccountInfoResponseEntity

    @POST("webapi/auth/login")
    @FormUrlEncoded
    suspend fun gosuslugiLogin(
        @Field("loginState") loginState: String,
        @Field("lscope") lscope: String,
        @Field("idp") idp: String = "esia",
        @Field("loginType") loginType: String = "8"
    ): LoginResponseEntity


}