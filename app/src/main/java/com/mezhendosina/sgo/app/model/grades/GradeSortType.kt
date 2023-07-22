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

import android.content.Context
import com.mezhendosina.sgo.app.R

object GradeSortType {
    const val BY_GRADE_VALUE = 0
    const val BY_GRADE_VALUE_DESC = 1
    const val BY_LESSON_NAME = 2

    fun toString(context: Context, type: Int): String {
        return context.getString(
            when (type) {
                BY_GRADE_VALUE -> R.string.from_good_to_bad_grade
                BY_GRADE_VALUE_DESC -> R.string.from_bad_to_good_grade
                BY_LESSON_NAME -> R.string.by_lesson_name
                else -> R.string.by_lesson_name
            }
        )
    }
}