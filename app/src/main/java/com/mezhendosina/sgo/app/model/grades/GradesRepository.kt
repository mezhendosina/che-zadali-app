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

package com.mezhendosina.sgo.app.model.grades

import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.Singleton.at
import com.mezhendosina.sgo.data.grades.GradesFromHtml
import com.mezhendosina.sgo.data.requests.sgo.grades.entities.GradesItem
import com.mezhendosina.sgo.data.requests.sgo.grades.entities.gradeOptions.GradeOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

typealias GradeActionListener = (grade: List<GradesItem>) -> Unit

class GradesRepository(
    private val gradesSource: GradesSource
) {

    private var grades = mutableListOf<GradesItem>()

    private val listeners = mutableSetOf<GradeActionListener>()

    suspend fun loadGradesOptions(): GradeOptions {
        val parentInfoLetter = gradesSource.getParentInfoLetter(at).body()?.string() ?: ""
        return GradesFromHtml().getOptions(parentInfoLetter)
    }


    suspend fun loadGrades(gradeOptions: GradeOptions, termid: String) {
        val at = Singleton.at
        val getGradesRequest = gradesSource.getGrades(
            at,
            gradeOptions.PCLID.value,
            gradeOptions.ReportType.first { it.is_selected }.value,
            termid,
            gradeOptions.SID.value
        ).body()?.string() ?: ""

        val gradesList = GradesFromHtml().extractGrades(getGradesRequest)

        grades = gradesList.filter { it.avg != null }.toMutableList()

        withContext(Dispatchers.Main) {
            Singleton.grades = grades
            notifyListeners()
        }
    }

    fun addListener(listener: GradeActionListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: GradeActionListener) {
        listeners.remove(listener)
    }

    private fun notifyListeners() {
        listeners.forEach { it.invoke(grades) }
    }

}