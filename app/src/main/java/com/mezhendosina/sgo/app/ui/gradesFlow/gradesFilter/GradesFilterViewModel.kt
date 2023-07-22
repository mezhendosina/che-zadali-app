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

package com.mezhendosina.sgo.app.ui.gradesFlow.gradesFilter

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mezhendosina.sgo.app.model.grades.GradeSortType
import com.mezhendosina.sgo.app.utils.toDescription
import com.mezhendosina.sgo.app.utils.toLiveData
import com.mezhendosina.sgo.data.SettingsDataStore
import com.mezhendosina.sgo.data.editPreference
import com.mezhendosina.sgo.data.getValue
import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton
import com.mezhendosina.sgo.data.netschool.api.settings.entities.YearListResponseEntity
import com.mezhendosina.sgo.data.netschool.repo.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GradesFilterViewModel(
    private val settingsRepository: SettingsRepository = NetSchoolSingleton.settingsRepository
) : ViewModel() {

    private val _gradesSortType: MutableLiveData<Int> = MutableLiveData()
    val gradesSortType = _gradesSortType.toLiveData()

    private val _yearList = MutableLiveData<List<YearListResponseEntity>>()
    val yearList = _yearList.toLiveData()

    private val _currentYear = MutableLiveData<YearListResponseEntity>()
    val currentYear = _currentYear.toLiveData()

    private val _errorDescription = MutableLiveData<String>()
    val errorDescription = _errorDescription.toLiveData()

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading = _isLoading.toLiveData()

    fun setGradeSort(context: Context, sortBy: Int) {
        viewModelScope.launch {
            SettingsDataStore.SORT_GRADES_BY.editPreference(
                context, sortBy
            )
        }
    }

    suspend fun getYearsList() {
        try {
            withContext(Dispatchers.Main) {
                _isLoading.value = true
            }
            val yearListResponse = settingsRepository.getYears().map {
                YearListResponseEntity(it.id, it.name + " год")
            }
            withContext(Dispatchers.Main) {
                _yearList.value = yearListResponse
                _currentYear.value =
                    if (NetSchoolSingleton.gradesYearId.value == null)
                        _yearList.value?.first { !it.name.contains("(*)") }
                    else NetSchoolSingleton.gradesYearId.value
                NetSchoolSingleton.gradesYearId.value = _currentYear.value
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                _errorDescription.value = e.toDescription()
            }
        } finally {
            withContext(Dispatchers.Main) {
                _isLoading.value = false
            }
        }
    }

    fun changeSelectedYear(id: YearListResponseEntity) {
        NetSchoolSingleton.gradesYearId.value = id
    }


    suspend fun updateYear() {
        try {
            settingsRepository.setYear(_currentYear.value?.id ?: -1)
            withContext(Dispatchers.Main) {
                NetSchoolSingleton.gradesYearId.value = _currentYear.value
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                _errorDescription.value = e.toDescription()
            }
        }
    }

    fun getGradeSort(context: Context) {
        viewModelScope.launch {
            _gradesSortType.value =
                SettingsDataStore.SORT_GRADES_BY.getValue(context, GradeSortType.BY_LESSON_NAME)
                    .first()
        }
    }

    fun changeTrimId(context: Context, id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            SettingsDataStore.CURRENT_TRIM_ID.editPreference(context, id)
        }
    }

    companion object {
        fun filterYearName(year: String): String = year.replace("(*) ", "")
    }
}
