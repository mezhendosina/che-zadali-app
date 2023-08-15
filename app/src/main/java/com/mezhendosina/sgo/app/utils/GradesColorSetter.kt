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

package com.mezhendosina.sgo.app.utils

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ItemGradeValueBinding

object GradesType {
    const val GOOD_GRADE = 2
    const val MID_GRADE = 1
    const val BAD_GRADE = 0
}

object GradeNames {
    const val FIVE = 5
    const val FOUR = 4
    const val THREE = 3
    const val TWO = 2
    const val ONE = 1
}

fun Float.toGradeType(): Int {
    return if (this <= 2.5f) {
        GradesType.BAD_GRADE
    } else if (2.5f <= this && this < 3.5f) {
        GradesType.MID_GRADE
    } else {
        GradesType.GOOD_GRADE
    }
}

fun ItemGradeValueBinding.setupWithBackground(context: Context, gradeType: Int, grade: String) {
    this.root.setupGradeBackground(context, gradeType)
    this.setupGrade(context, gradeType, grade, true)
}

fun ItemGradeValueBinding.setupGrade(
    context: Context,
    gradeType: Int,
    grade: String,
    onContainer: Boolean = false
) {
    val color = getGradeBackgroundColor(context, gradeType, onContainer)
    this.value.setTextColor(color)

    val outGradeText =
        if (grade.endsWith(",00")) grade.dropLast(3)
        else if (grade.endsWith(".0")) grade.dropLast(2)
        else if (grade.endsWith("0")) grade.dropLast(1)
        else grade

    this.value.text = outGradeText
}


fun View.setupGradeBackground(context: Context, gradeType: Int) {
    this.setBackgroundResource(
        when (gradeType) {
            GradesType.GOOD_GRADE -> R.drawable.shape_good_grade
            GradesType.MID_GRADE -> R.drawable.shape_mid_grade
            GradesType.BAD_GRADE -> R.drawable.shape_bad_grade
            else -> 0
        }
    )
//    Blurry.with(context).capture(this)
}

fun TextView.setupColorWithGrade(context: Context, gradeType: Int) {
    val color = getGradeBackgroundColor(context, gradeType, true)
    this.setTextColor(color)
}


private fun getGradeBackgroundColor(context: Context, gradeType: Int, onContainer: Boolean): Int {
    val gradeColor = TypedValue()

    val gradeColorResource =
        if (!onContainer) {
            when (gradeType) {
                GradesType.GOOD_GRADE -> R.attr.colorGoodGrade
                GradesType.MID_GRADE -> R.attr.colorMidGrade
                GradesType.BAD_GRADE -> com.google.android.material.R.attr.colorError
                else -> 0
            }
        } else {
            when (gradeType) {
                GradesType.GOOD_GRADE -> R.attr.colorOnGoodGradeContainer
                GradesType.MID_GRADE -> R.attr.colorOnMidGradeContainer
                GradesType.BAD_GRADE -> com.google.android.material.R.attr.colorOnErrorContainer
                else -> 0
            }
        }

    context.theme.resolveAttribute(gradeColorResource, gradeColor, true)
    return gradeColor.data
}
