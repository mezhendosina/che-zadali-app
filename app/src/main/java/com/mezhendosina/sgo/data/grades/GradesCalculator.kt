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

package com.mezhendosina.sgo.data.grades

import androidx.lifecycle.MutableLiveData
import com.mezhendosina.sgo.app.ui.gradesFlow.gradeItem.GradeItemFragment
import com.mezhendosina.sgo.data.netschool.api.grades.entities.GradesItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class CalculateGradeItem(
    val countFive: Int,
    val countFour: Int,
    val countThree: Int,
    val countTwo: Int,
) {
    private fun count(): Int = countFive + countFour + countThree + countTwo
    fun avg(): Float =
        (countFive * 5 + countFour * 4 + countThree * 3 + countTwo * 2).toFloat() / count().toFloat()

    fun changeGrade(grade: Int, delta: Int): CalculateGradeItem {
        return when (grade) {
            GradeItemFragment.FIVE_GRADE -> CalculateGradeItem(
                countFive + delta,
                countFour,
                countThree,
                countTwo
            )

            GradeItemFragment.FOUR_GRADE -> CalculateGradeItem(
                countFive,
                countFour + delta,
                countThree,
                countTwo
            )

            GradeItemFragment.THREE_GRADE -> CalculateGradeItem(
                countFive,
                countFour,
                countThree + delta,
                countTwo
            )

            GradeItemFragment.TWO_GRADE -> CalculateGradeItem(
                countFive,
                countFour,
                countThree,
                countTwo + delta
            )

            else -> this
        }
    }

    fun toList(): MutableList<Int> = mutableListOf(countFive, countFour, countThree, countTwo)
    fun toGradeItem(): GradesItem =
        GradesItem(
            "",
            countFive,
            countFour,
            countThree,
            countTwo,
            0,
            avg().toString().replace(".", ",")
        )
}

data class GradeItem(
    val value: Int,
    val weight: Int
)

class GradesCalculator(initGradesList: List<GradeItem>?) {

    val avgGrade = MutableLiveData<Float?>()
    val gradeList = MutableStateFlow(initGradesList ?: emptyList())

    init {
        CoroutineScope(Dispatchers.IO).launch {
            gradeList.collect {
                calculateGrade()
            }
        }
    }

    private suspend fun calculateGrade() {
        withContext(Dispatchers.Main) {
            avgGrade.value = null
        }
        var sumGrades = 0
        var sumWeight = 0
        gradeList.last().forEach {
            withContext(Dispatchers.Main) {
                sumGrades += it.value * it.weight
                sumWeight += it.weight
            }
        }
        withContext(Dispatchers.Main) {
            avgGrade.value = (sumGrades / sumWeight).toFloat()
        }
    }

    fun addGrade(grade: GradeItem) {
        gradeList.update { it.plus(grade) }
    }

    fun deleteGrade(grade: GradeItem) {
        gradeList.update { it.minus(grade) }
    }
}