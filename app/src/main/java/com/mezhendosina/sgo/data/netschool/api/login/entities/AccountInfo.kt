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

package com.mezhendosina.sgo.data.netschool.api.login.entities

import com.google.gson.annotations.SerializedName
import com.mezhendosina.sgo.data.requests.sgo.login.entities.CurrentOrganization
import com.mezhendosina.sgo.data.requests.sgo.login.entities.Organization
import com.mezhendosina.sgo.data.requests.sgo.login.entities.User

data class AccountInfo(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("active")
    val active: Boolean,
    @SerializedName("activeToken")
    val activeToken: Any,
    @SerializedName("canLogin")
    val canLogin: Boolean,
    @SerializedName("currentOrganization")
    val currentOrganization: CurrentOrganization,
    @SerializedName("loginTime")
    val loginTime: String,
    @SerializedName("organizations")
    val organizations: List<Organization>,
    @SerializedName("secureActiveToken")
    val secureActiveToken: String,
    @SerializedName("storeTokens")
    val storeTokens: Boolean,
    @SerializedName("user")
    val user: User,
    @SerializedName("userRoles")
    val userRoles: List<Any>,
)
