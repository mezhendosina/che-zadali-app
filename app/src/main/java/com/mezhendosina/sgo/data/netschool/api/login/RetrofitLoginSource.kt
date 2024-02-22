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
import com.mezhendosina.sgo.data.netschool.base.BaseRetrofitSource
import com.mezhendosina.sgo.data.netschool.base.RetrofitConfig
import com.mezhendosina.sgo.data.netschool.base.toMD5
import com.mezhendosina.sgo.data.requests.sgo.login.entities.GetDataResponseEntity
import com.mezhendosina.sgo.data.requests.sgo.login.entities.LoginResponseEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitLoginSource
    @Inject
    constructor(
        config: RetrofitConfig,
    ) : BaseRetrofitSource(config), LoginSource {
        private val loginApi = retrofit.create(LoginApi::class.java)

        override suspend fun loginData() = wrapRetrofitExceptions(false) { loginApi.loginData() }

        override suspend fun getSchools(query: String): List<SchoolEntity> =
            wrapRetrofitExceptions(false) {
                val schools = loginApi.getSchools(query)

                val filterSchools =
                    schools.filter { it.name.contains("МБОУ|МКОУ|СОШ|МАОУ|МКШ|СУНЦ|ШНОР|ШВОР|ГБПОУ|ГКОУ|ГБОУ|МОУ".toRegex()) }
                if (schools.isNotEmpty() && filterSchools.isEmpty()) schools else filterSchools
            }

        override suspend fun getData(): GetDataResponseEntity =
            wrapRetrofitExceptions(false) {
                loginApi.getData()
            }

        override suspend fun login(loginEntity: LoginEntity): LoginResponseEntity =
            wrapRetrofitExceptions(false) {
                val passwordMD5 =
                    (loginEntity.salt + loginEntity.password).toMD5()

                val resp =
                    loginApi.login(
                        loginType = 1,
                        scid = loginEntity.schoolId,
                        UN = loginEntity.login,
                        PW = passwordMD5.slice(0 until 6),
                        pw2 = passwordMD5,
                        lt = loginEntity.lt,
                        ver = loginEntity.ver,
                    )
                com.mezhendosina.sgo.Singleton.at = resp.at
                com.mezhendosina.sgo.Singleton.loggedIn = true
                resp
            }

        override suspend fun getStudents(): List<StudentResponseEntity>? =
            wrapRetrofitExceptions {
                loginApi.getStudents().body()
            }

        override suspend fun logout() =
            wrapRetrofitExceptions {
                loginApi.logout(com.mezhendosina.sgo.Singleton.at)
                com.mezhendosina.sgo.Singleton.loggedIn = false
            }
    }
