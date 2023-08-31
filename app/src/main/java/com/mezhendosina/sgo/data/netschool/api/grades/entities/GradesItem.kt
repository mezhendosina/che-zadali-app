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

package com.mezhendosina.sgo.data.netschool.api.grades.entities

import com.mezhendosina.sgo.app.model.grades.entities.CountGradeEntity
import com.mezhendosina.sgo.app.utils.GradeNames
import com.mezhendosina.sgo.app.utils.GradesType
import com.mezhendosina.sgo.data.grades.CalculateGradeItem

data class GradesItem(
    val name: String,
    val five: Int?,
    val four: Int?,
    val three: Int?,
    val two: Int?,
    val one: Int?,
    val avg: String?
) {
    fun avgGrade(): Float = avg?.replace(",", ".")?.toFloat() ?: 0f

    fun countGrades(): Int = (five ?: 0) + (four ?: 0) + (three ?: 0) + (two ?: 0) + (one ?: 0)

    fun toCalculateItem(): CalculateGradeItem = CalculateGradeItem(
        five ?: 0,
        four ?: 0,
        three ?: 0,
        two ?: 0
    )

    //TODO rewrite
    fun countGradesToList(): List<CountGradeEntity> {
        val outList = mutableListOf<CountGradeEntity>()
        if (five != null) outList.add(
            CountGradeEntity(
                "Пятерок",
                GradeNames.FIVE,
                GradesType.GOOD_GRADE,
                five
            )
        )
        if (four != null) outList.add(
            CountGradeEntity(
                "Четверок",
                GradeNames.FOUR,
                GradesType.GOOD_GRADE,
                four
            )
        )
        if (three != null) outList.add(
            CountGradeEntity(
                "Троек",
                GradeNames.THREE,
                GradesType.MID_GRADE,
                three
            )
        )
        if (two != null) outList.add(
            CountGradeEntity(
                "Двоек",
                GradeNames.TWO,
                GradesType.BAD_GRADE,
                two
            )
        )
        if (one != null) outList.add(
            CountGradeEntity(
                "Единиц",
                GradeNames.ONE,
                GradesType.BAD_GRADE,
                one
            )
        )
        return outList
    }


}