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

package com.mezhendosina.sgo.data.netschool.api.settings.entities

data class MySettingsResponseEntity(
    val birthDate: String,
    val email: String?,
    val existsPhoto: Boolean,
    val firstName: String,
    val lastName: String,
    val loginName: String,
    val middleName: String?,
    val mobilePhone: String?,
    val preferedCom: String,
    val roles: List<String>,
    val schoolyearId: Int,
    val userId: Int,
    val userSettings: UserSettingsEntity,
    val windowsAccount: Any
) {
    fun toRequestEntity(): MySettingsRequestEntity = MySettingsRequestEntity(
        email, mobilePhone, schoolyearId, userId, userSettings, windowsAccount
    )
}