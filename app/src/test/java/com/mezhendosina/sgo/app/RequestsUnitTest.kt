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

package com.mezhendosina.sgo.app

import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.model.grades.GradesRepository
import com.mezhendosina.sgo.app.model.login.LoginEntity
import com.mezhendosina.sgo.data.grades.GradesFromHtml
import com.mezhendosina.sgo.data.requests.sgo.SourceProviderHolder
import com.mezhendosina.sgo.data.toMD5
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class RequestsUnitTest {

    private val sourcesProvider = SourceProviderHolder.sourcesProvider

    private val loginSource = sourcesProvider.getLoginSource()
    private val gradesSource = sourcesProvider.getGradesSource()
//    private val loginRepository = LoginRepository(loginSource)

    private val singleton = Singleton

    @Before
    fun login() {
        singleton.baseUrl = "https://sgo.edu-74.ru/"
        runBlocking {
            loginSource.loginData()
            val getData = loginSource.getData()

            val login = loginSource.login(
                LoginEntity(
                    89,
                    "МеньшенинЕ1",
                    "285639".toMD5(),
                    getData.lt,
                    getData.salt,
                    getData.ver
                )
            )
            singleton.at = login.at
//            loginRepository.at = login.at
        }
    }

    @Test
    fun grades() {
        val gradesRepository = GradesRepository(gradesSource)

        runBlocking {
            val gradeOptions = gradesRepository.loadGradesOptions()
            val getGradesRequest = gradesSource.getGrades(
                singleton.at,
                gradeOptions.PCLID.value,
                gradeOptions.ReportType.first { it.is_selected }.value,
                gradeOptions.TERMID.first().value,
                gradeOptions.SID.value
            ).body()?.string() ?: ""

            val gradesList = GradesFromHtml().extractGrades(getGradesRequest)
            println(gradesList)

        }
    }

    @After
    fun logout() {
        runBlocking {
//            loginRepository.logout()

        }
    }

}

