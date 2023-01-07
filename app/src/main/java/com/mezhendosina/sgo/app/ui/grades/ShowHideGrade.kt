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

package com.mezhendosina.sgo.app.ui.grades

import android.view.View
import com.mezhendosina.sgo.app.databinding.ItemGradeValueBinding

fun showBadGrade(binding: ItemGradeValueBinding) {
    binding.apply {
        badGrade.visibility = View.VISIBLE

        midGrade.visibility = View.GONE
        goodGrade.visibility = View.GONE
        dutyMark.visibility = View.GONE
    }
}

fun showMidGrade(binding: ItemGradeValueBinding) {
    binding.apply {
        midGrade.visibility = View.VISIBLE

        goodGrade.visibility = View.GONE
        badGrade.visibility = View.GONE
        dutyMark.visibility = View.GONE

    }
}

fun showGoodGrade(binding: ItemGradeValueBinding) {
    binding.apply {
        goodGrade.visibility = View.VISIBLE

        midGrade.visibility = View.GONE
        badGrade.visibility = View.GONE
        dutyMark.visibility = View.GONE
    }
}