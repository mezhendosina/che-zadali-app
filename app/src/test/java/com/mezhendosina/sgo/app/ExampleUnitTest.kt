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

import androidx.lifecycle.MutableLiveData
import com.mezhendosina.sgo.data.grades.CalculateGradeItem
import com.mezhendosina.sgo.data.grades.GradesCalculator
import com.mezhendosina.sgo.data.grades.GradesFromHtml
import com.mezhendosina.sgo.data.requests.sgo.grades.entities.GradesItem
import org.junit.Test
import java.io.File

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun testExtractGradeOptions() {
        val f = File("D:/Programming/gradesOptions.html").readText()
        println(GradesFromHtml().getOptions(f))

    }

    @Test
    fun testExtractGrades() {
        val f = File("D:/Programming/extractGrades.html").readText()
        println(GradesFromHtml().extractGrades(f))
    }

    @Test
    fun testInitCalculator() {
        val gradeItem = GradesItem("", 2, 2, 2, 2, null, "3,5")
        val _oldCalculatedGrade = MutableLiveData<CalculateGradeItem>()


        val gradesCalculator = GradesCalculator(gradeItem)

        println(gradesCalculator.autoCalculateGrade(4.5f))
        println(
            when (2f) {
                in 0f..2.5f -> "123"
                else -> "ok"
            }
        )
//
//        if (gradeItem.avg != null) {
//            val avgGrade = gradeItem.avg!!.replace(",", ".").toFloat()
//            if (avgGrade < 2.5) {
//                _oldCalculatedGrade.value =
//                    gradesCalculator.autoCalculateGrade(2.5f)
//            } else if (avgGrade < 3.5) {
//                _oldCalculatedGrade.value =
//                    gradesCalculator.autoCalculateGrade(3.5f)
//            } else if (avgGrade < 4.5) {
//                _oldCalculatedGrade.value =
//                    gradesCalculator.autoCalculateGrade(4.5f)
//            }
//            println(_oldCalculatedGrade.value)
//        }
    }
//
//    @Test
//    fun testGradesCalculator() {
//        println(
//            GradesCalculator(
//                GradesItem(
//                    "123",
//                    4,
//                    7,
//                    0,
//                    0,
//                    0,
//                    "3,43"
//                )
//            ).calculateGrade(
//                GradesCalculator.FIVE_GRADE,
//                4.5f
//            )
//        )
//    }

}