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

package com.mezhendosina.sgo.data.netschool.repo

import android.content.Context
import com.mezhendosina.sgo.app.uiEntities.SchoolUiEntity
import com.mezhendosina.sgo.data.SettingsDataStore
import com.mezhendosina.sgo.data.editPreference
import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton
import com.mezhendosina.sgo.data.netschool.api.login.LoginEntity
import com.mezhendosina.sgo.data.netschool.api.login.LoginSource
import com.mezhendosina.sgo.data.netschool.api.settings.SettingsSource
import com.mezhendosina.sgo.data.requests.sgo.login.entities.LogoutRequestEntity
import com.mezhendosina.sgo.data.requests.sgo.login.entities.StudentResponseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext

class LoginRepository(
    private val loginSource: LoginSource,
    private val settingsSource: SettingsSource
) {
    val schools = MutableSharedFlow<List<SchoolUiEntity>>()

    suspend fun findSchool(name: String) {
        val schoolsList = loginSource.getSchools(name).map { it.toUiEntity() }

        withContext(Dispatchers.Main) {
            schools.emit(schoolsList)
            NetSchoolSingleton.schools = schoolsList
        }
    }

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

        NetSchoolSingleton.at = loginRequest.at
        val studentsRequest = loginSource.getStudents()
        withContext(Dispatchers.IO) {
            if (firstLogin) {
                withContext(Dispatchers.Main) {
                    if (studentsRequest != null) {
                        if (studentsRequest.size <= 1) {
                            SettingsDataStore.CURRENT_USER_ID.editPreference(
                                context,
                                studentsRequest.first().id
                            )
                            onOneUser.invoke()
                            SettingsDataStore().saveALl(context, loginEntity)
                        } else {
                            onMoreUser.invoke(studentsRequest)
                            SettingsDataStore().saveALl(context, loginEntity, false)
                        }
                    } else {
                        SettingsDataStore.CURRENT_USER_ID.editPreference(
                            context,
                            loginRequest.accountInfo.user.id
                        )
                        onOneUser.invoke()
                        SettingsDataStore().saveALl(context, loginEntity)
                    }
                }
            }
            val yearsID = settingsSource.getYearList().first { !it.name.contains("(*)") }
            withContext(Dispatchers.Main) {
                NetSchoolSingleton.journalYearId.value = yearsID.id
            }
        }
    }

    suspend fun logout() = loginSource.logout(LogoutRequestEntity(NetSchoolSingleton.at))

}