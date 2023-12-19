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

package com.mezhendosina.sgo.app.ui.gradesFlow.gradeItem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mezhendosina.sgo.app.model.grades.GradesRepository
import com.mezhendosina.sgo.app.model.grades.GradesRepositoryInterface
import com.mezhendosina.sgo.app.utils.toLiveData
import com.mezhendosina.sgo.data.grades.CalculateGradeItem
import com.mezhendosina.sgo.data.netschool.api.grades.entities.GradesItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GradeItemViewModel @Inject constructor(
    val gradesRepository: GradesRepositoryInterface
) : ViewModel() {

    private val _calculatedGrade = MutableLiveData<CalculateGradeItem>()
    val calculatedGrade: LiveData<CalculateGradeItem> = _calculatedGrade

    private val _grade = MutableLiveData<CalculateGradeItem>()
    val grade: LiveData<CalculateGradeItem> = _grade

    private val _lesson = MutableLiveData<GradesItem>()
    val lesson = _lesson.toLiveData()

    init {
        viewModelScope.launch {
            gradesRepository.selectedGradesItem.collect {
                _lesson.value = it
                initCalculator()
            }
        }
    }

    private fun initCalculator() {
        _calculatedGrade.value = _lesson.value?.toCalculateItem()
        _grade.value = _calculatedGrade.value
    }

    fun editGrade(grade: Int, delta: Int) {
        _calculatedGrade.value = _calculatedGrade.value?.changeGrade(grade, delta)
    }

}