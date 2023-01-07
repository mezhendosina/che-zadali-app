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

package com.mezhendosina.sgo.app.model.login

import android.content.Context
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.model.settings.SettingsSource
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.requests.sgo.login.entities.LogoutRequestEntity
import com.mezhendosina.sgo.data.requests.sgo.login.entities.StudentResponseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginRepository(
    private val loginSource: LoginSource,
    private val settingsSource: SettingsSource
) {

    var at: String? = null

    suspend fun login(
        context: Context,
        login: String,
        password: String,
        schoolId: Int,
        firstLogin: Boolean = true,
        onOneUser: () -> Unit = {},
        onMoreUser: (List<StudentResponseEntity>) -> Unit = {}
    ) {
        loginSource.loginData()
        val getData = loginSource.getData()
        val loginEntity = LoginEntity(
            schoolId,
            login,
            password,
            getData.lt,
            getData.salt,
            getData.ver
        )
        val loginRequest = loginSource.login(loginEntity)

        at = loginRequest.at
        Singleton.at = loginRequest.at

        val studentsRequest = loginSource.getStudents()
        withContext(Dispatchers.IO) {
            if (firstLogin) {
                val settings = Settings(context)
                withContext(Dispatchers.Main) {
                    if (studentsRequest != null) {
                        if (studentsRequest.size <= 1) {
                            settings.setCurrentUserId(studentsRequest.first().id)
                            onOneUser.invoke()
                            settings.saveALl(loginEntity)
                        } else {
                            onMoreUser.invoke(studentsRequest)
                            settings.saveALl(loginEntity, false)
                        }
                    } else {
                        settings.setCurrentUserId(loginRequest.accountInfo.user.id)
                        onOneUser.invoke()
                        settings.saveALl(loginEntity)
                    }
                }
            }
            val yearsID = settingsSource.getYearList().first { !it.name.contains("(*)") }.id
            withContext(Dispatchers.Main) {
                Singleton.currentYearId.value = yearsID
            }
        }
    }

    suspend fun logout() = loginSource.logout(LogoutRequestEntity(at.toString()))
}