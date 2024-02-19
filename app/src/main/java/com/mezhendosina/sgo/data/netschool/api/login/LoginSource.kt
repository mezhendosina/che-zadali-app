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
import com.mezhendosina.sgo.data.requests.sgo.login.entities.GetDataResponseEntity
import com.mezhendosina.sgo.data.requests.sgo.login.entities.LoginResponseEntity

interface LoginSource {
    suspend fun loginData()

    suspend fun getSchools(query: String): List<SchoolEntity>

    suspend fun getData(): GetDataResponseEntity

    suspend fun login(loginEntity: LoginEntity): LoginResponseEntity

    suspend fun getStudents(): List<StudentResponseEntity>?

    suspend fun logout()
}
