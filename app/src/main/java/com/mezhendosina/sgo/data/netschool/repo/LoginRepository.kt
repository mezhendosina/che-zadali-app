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

import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.uiEntities.SchoolUiEntity
import com.mezhendosina.sgo.data.SettingsDataStore
import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton
import com.mezhendosina.sgo.data.netschool.api.login.LoginEntity
import com.mezhendosina.sgo.data.netschool.api.login.LoginSource
import com.mezhendosina.sgo.data.netschool.api.login.entities.StudentResponseEntity
import com.mezhendosina.sgo.data.netschool.api.login.entities.accountInfo.User
import com.mezhendosina.sgo.data.netschool.api.settings.SettingsSource
import com.mezhendosina.sgo.data.requests.sgo.login.entities.LoginResponseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val loginSource: LoginSource,
    private val settingsDataStore: SettingsDataStore,
    private val settingsSource: SettingsSource
) : LoginRepositoryInterface {
    private val _schools = MutableStateFlow<List<SchoolUiEntity>>(emptyList())
    override suspend fun findSchool(schoolId: Int): SchoolUiEntity {
        return _schools.last().first{ it.id == schoolId}
    }


    override fun getSchools(): StateFlow<List<SchoolUiEntity>> {
        return _schools
    }


    override suspend fun mapSchools(name: String) {
        val schoolsList = loginSource.getSchools(name).map { it.toUiEntity() }

        withContext(Dispatchers.Main) {
            _schools.emit(schoolsList)
        }
    }

    override suspend fun login(
        login: String?,
        password: String?,
        schoolId: Int?,
        firstLogin: Boolean,
        onOneUser: () -> Unit,
        onMoreUser: (List<StudentResponseEntity>) -> Unit
    ) {


        loginSource.loginData()
        val getData = loginSource.getData()

        val loginEntity =
            if (login.isNullOrEmpty() || password.isNullOrEmpty() || schoolId == null) {
                LoginEntity(
                    settingsDataStore.getValue(SettingsDataStore.SCHOOL_ID).first() ?: -1,
                    settingsDataStore.getValue(SettingsDataStore.LOGIN).first() ?: "",
                    settingsDataStore.getValue(SettingsDataStore.PASSWORD).first() ?: "",
                    getData.lt,
                    getData.salt,
                    getData.ver
                )
            } else {
                LoginEntity(
                    schoolId,
                    login,
                    password,
                    getData.lt,
                    getData.salt,
                    getData.ver
                )
            }
        val loginRequest = loginSource.login(loginEntity)

        postLogin(loginEntity, loginRequest, firstLogin, onOneUser, onMoreUser)
    }

    private suspend fun postLogin(
        loginEntity: LoginEntity,
        loginRequest: LoginResponseEntity,
        firstLogin: Boolean,
        onOneUser: () -> Unit,
        onMoreUser: (List<StudentResponseEntity>) -> Unit
    ) {
        val studentsRequest = loginSource.getStudents()
        withContext(Dispatchers.IO) {
            if (firstLogin) {
                withContext(Dispatchers.Main) {
                    if (studentsRequest != null) {
                        if (studentsRequest.size <= 1) {
                            settingsDataStore.setValue(
                                SettingsDataStore.CURRENT_USER_ID,
                                studentsRequest.first().id
                            )
                            onOneUser.invoke()
                            settingsDataStore.saveLogin(loginEntity)
                        } else {
                            onMoreUser.invoke(studentsRequest)
                            settingsDataStore.saveLogin(loginEntity, false)
                        }
                    } else {
                        settingsDataStore.setValue(
                            SettingsDataStore.CURRENT_USER_ID,
                            loginRequest.accountInfo.user.id
                        )
                        onOneUser.invoke()
                        settingsDataStore.saveLogin(loginEntity)
                    }
                }
            }
            val yearsID = settingsSource.getYearList().first { !it.name.contains("(*)") }
            withContext(Dispatchers.Main) {
                NetSchoolSingleton.journalYearId.value = yearsID.id
            }
        }
    }

    override suspend fun getGosuslugiUsers(loginState: String): List<User> =
        loginSource.getGosuslugiAccountInfo(loginState).users

    override suspend fun gosuslugiLogin(firstLogin: Boolean) {
        val loginState =
            settingsDataStore.getValue(SettingsDataStore.ESIA_LOGIN_STATE).first() ?: ""
        val userId = settingsDataStore.getValue(SettingsDataStore.ESIA_USER_ID).first() ?: ""

        val login = if (!firstLogin) {
            loginSource.crossLogin()
            loginSource.gosuslugiLogin(loginState, userId)
        } else {
            loginSource.gosuslugiLogin(loginState, userId)
        }

        withContext(Dispatchers.Main) {
            if (firstLogin) {
                settingsDataStore.saveEsiaLogin(loginState, userId)
                settingsDataStore.setValue(SettingsDataStore.LOGGED_IN, true)
            }
            Singleton.at = login.at
        }
    }

    override suspend fun logout() = loginSource.logout()

}