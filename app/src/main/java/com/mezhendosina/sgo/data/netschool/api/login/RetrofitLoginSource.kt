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

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mezhendosina.sgo.app.BuildConfig
import com.mezhendosina.sgo.app.netschool.api.login.entities.SchoolEntity
import com.mezhendosina.sgo.app.netschool.base.RetrofitConfig
import com.mezhendosina.sgo.data.netschool.NetSchoolExpectedResults
import com.mezhendosina.sgo.data.netschool.api.login.entities.accountInfo.AccountInfoResponseEntity
import com.mezhendosina.sgo.data.netschool.base.BaseRetrofitSource
import com.mezhendosina.sgo.data.netschool.base.toMD5
import com.mezhendosina.sgo.data.requests.sgo.login.entities.GetDataResponseEntity
import com.mezhendosina.sgo.data.requests.sgo.login.entities.LoginResponseEntity
import com.mezhendosina.sgo.data.requests.sgo.login.entities.LogoutRequestEntity
import com.mezhendosina.sgo.data.requests.sgo.login.entities.StudentResponseEntity

class RetrofitLoginSource(
    config: RetrofitConfig
) : BaseRetrofitSource(config), LoginSource {

    private val loginApi = retrofit.create(LoginApi::class.java)

    override suspend fun loginData() = wrapRetrofitExceptions { loginApi.loginData() }

    override suspend fun getSchools(query: String): List<SchoolEntity> =
        wrapRetrofitExceptions {
            val schools =
                if (!BuildConfig.DEBUG)
                    loginApi.getSchools(query)
                else {
                    val itemsListType = object : TypeToken<List<SchoolEntity>>() {}.type
                    Gson().fromJson(NetSchoolExpectedResults.schools, itemsListType)
                }
            schools.filter { it.name.contains("(МБОУ)|(МКОУ)|(СОШ)|(МАОУ)|(МКШ)|(СУНЦ)|(ШНОР)|(ШВОР)|(ГБПОУ)|(ГКОУ)".toRegex()) }
        }

    override suspend fun getData(): GetDataResponseEntity =
        wrapRetrofitExceptions {
            loginApi.getData()
        }

    override suspend fun login(loginEntity: LoginEntity): LoginResponseEntity =
        wrapRetrofitExceptions {
            val passwordMD5 =
                (loginEntity.salt + loginEntity.password).toMD5()

            loginApi.login(
                loginType = 1,
                scid = loginEntity.schoolId,
                UN = loginEntity.login,
                PW = passwordMD5.slice(0 until 6),
                pw2 = passwordMD5,
                lt = loginEntity.lt,
                ver = loginEntity.ver
            )
        }

    override suspend fun getAccountInfo(loginState: String): AccountInfoResponseEntity =
        wrapRetrofitExceptions {
            loginApi.getAccountInfo(loginState)
        }

    override suspend fun gosuslugiLogin(loginState: String, userId: String): LoginResponseEntity =
        wrapRetrofitExceptions {
            loginApi.gosuslugiLogin(loginState, userId)
        }


    override suspend fun getStudents(): List<StudentResponseEntity>? =
        wrapRetrofitExceptions {
            loginApi.getStudents().body()
        }

    override suspend fun logout(logoutRequestEntity: LogoutRequestEntity) =
        wrapRetrofitExceptions {
            loginApi.logout(logoutRequestEntity.at)
        }
}