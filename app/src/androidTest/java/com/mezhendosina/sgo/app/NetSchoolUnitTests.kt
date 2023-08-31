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

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mezhendosina.sgo.app.netschool.api.login.entities.SchoolEntity
import com.mezhendosina.sgo.data.netschool.NetSchoolExpectedResults
import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NetSchoolUnitTests : NetSchoolTestInterface {

    private val netSchoolSingleton = NetSchoolSingleton

    @Before
    override fun login() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val login = System.getenv("SGO_LOGIN")
        val password = System.getenv("SGO_PASSWORD")
        runBlocking {
            if (!login.isNullOrEmpty() && !password.isNullOrEmpty()) {
                netSchoolSingleton.loginRepository.login(
                    appContext,
                    login,
                    password,
                    89
                )
            } else {
                throw RuntimeException("Login or password is null or empty")
            }
        }
    }

    @Test
    override fun region() {
        assertEquals(
            NetSchoolExpectedResults.region,
            netSchoolSingleton.regionsRepository.getRegions()
        )
    }

    @Test
    override fun school() {
        runBlocking {
            netSchoolSingleton.loginRepository.findSchool("68")
            val itemsListType = object : TypeToken<List<SchoolEntity>>() {}.type
            assertEquals(
                netSchoolSingleton.schools,
                Gson().fromJson(NetSchoolExpectedResults.schools, itemsListType)
            )
        }
    }

    @Test
    override fun announcements() {
        runBlocking {
            netSchoolSingleton.announcementsRepository.announcements()
        }
    }

    @Test
    override fun attachments() {

    }

    @Test
    override fun grades() {
    }

    @Test
    override fun diary() {
    }

    @Test
    override fun settings() {
    }

    @After
    override fun logout() {
    }


}