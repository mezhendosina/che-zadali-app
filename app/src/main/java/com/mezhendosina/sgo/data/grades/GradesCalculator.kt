package com.mezhendosina.sgo.data.grades

import com.mezhendosina.sgo.data.requests.grades.entities.GradesItem
import kotlin.math.roundToInt

data class ChangeGradeItem(
    val five: Int,
    val four: Int,
    val three: Int,
    val avg: Double
) {
    fun avgGrade(countTwo: Int): Float =
        (this.five * 5 + this.five * 4 + this.three * 3 + countTwo * 2).toFloat() / (this.five + this.four + this.three + countTwo).toFloat()
}

class GradesCalculator(
    private val gradesItem: GradesItem
) {
    fun calculateGrade(
        changeTo: Float
    ): ChangeGradeItem {
        val changeGradeItem = mutableListOf(0, 0, 0)
        if (changeTo <= 2.5f) {
            changeGradeItem[0] = doCalc(FIVE_GRADE, changeTo)
            changeGradeItem[1] = doCalc(FOUR_GRADE, changeTo)
            changeGradeItem[2] = doCalc(THREE_GRADE, changeTo)
        } else if (changeTo <= 4.0f) {
            changeGradeItem[0] = doCalc(FIVE_GRADE, changeTo)
            changeGradeItem[1] = doCalc(FOUR_GRADE, changeTo)
        } else if (changeTo > 4.0f)
            changeGradeItem[0] = doCalc(FIVE_GRADE, changeTo)

        return ChangeGradeItem(
            changeGradeItem[0],
            changeGradeItem[1],
            changeGradeItem[2],
            changeTo.toDouble()
        )
    }

    // formula created by https://github.com/0ladyshek
    private fun doCalc(i: Int, changeTo: Float): Int {
        return ((gradesItem.countGrades() * (gradesItem.avgGrade() - changeTo)) / (changeTo - i)).roundToInt()
    }

    private fun List<Int>.avgGrade(): Float =
        (this[0] * 5 + this[1] * 4 + this[2] * 3 + this[3] * 2).toFloat() / (this[0] + this[1] + this[2] + this[3]).toFloat()

    companion object {
        const val FIVE_GRADE = 5
        const val FOUR_GRADE = 4
        const val THREE_GRADE = 3
        const val TWO_GRADE = 3
    }

}
