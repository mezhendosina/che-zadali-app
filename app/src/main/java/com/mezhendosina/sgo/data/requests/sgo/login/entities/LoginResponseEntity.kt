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

package com.mezhendosina.sgo.data.requests.sgo.login.entities


import com.google.gson.annotations.SerializedName

data class LoginResponseEntity(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("accountInfo")
    val accountInfo: AccountInfo,
    @SerializedName("at")
    val at: String,
    @SerializedName("code")
    val code: Any,
    @SerializedName("entryPoint")
    val entryPoint: String,
    @SerializedName("errorMessage")
    val errorMessage: Any,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("requestData")
    val requestData: RequestData,
    @SerializedName("timeOut")
    val timeOut: Int,
    @SerializedName("tokenType")
    val tokenType: String
)