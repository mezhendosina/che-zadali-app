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

package com.mezhendosina.sgo.app.model.chooseSchool

import com.mezhendosina.sgo.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

typealias schoolsActionListener = (List<SchoolUiEntity>) -> Unit

class ChooseSchoolRepository(private val schoolsSource: SchoolsSource) {

    var schools = mutableListOf<SchoolUiEntity>()

    private val listeners = mutableSetOf<schoolsActionListener>()

    suspend fun findSchool(name: String) {
        val schoolsList = schoolsSource.getSchools(name).map { it.toUiEntity() }

        withContext(Dispatchers.Main) {
            schools = schoolsList.toMutableList()
            Singleton.schools = schools
            notifyListeners()
        }
    }

    fun addListener(listener: schoolsActionListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: schoolsActionListener) {
        listeners.remove(listener)
    }

    private fun notifyListeners() {
        listeners.forEach { it.invoke(schools) }
    }


}