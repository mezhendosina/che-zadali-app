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

package com.mezhendosina.sgo.app.ui.gradesFilter

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.model.settings.SettingsRepository
import com.mezhendosina.sgo.app.utils.toDescription
import com.mezhendosina.sgo.app.utils.toLiveData
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.requests.sgo.settings.entities.YearListResponseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GradesFilterViewModel(
    private val settingsRepository: SettingsRepository = Singleton.settingsRepository
) : ViewModel() {

    private val _gradesSortType = MutableLiveData<Int>()
    val gradesSortType = _gradesSortType.toLiveData()

    private val _yearList = MutableLiveData<List<YearListResponseEntity>>()
    val yearList = _yearList.toLiveData()

    private val _selectedYear = MutableLiveData<YearListResponseEntity>()
    val selectedYear = _selectedYear.toLiveData()

    private val _errorDescription = MutableLiveData<String>()
    val errorDescription = _errorDescription.toLiveData()

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading = _isLoading.toLiveData()

    fun setGradeSort(context: Context, sortBy: Int) {
        val settings = Settings(context)
        viewModelScope.launch {
            settings.editPreference(
                Settings.SORT_GRADES_BY, sortBy
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
                _selectedYear.value = _yearList.value?.first { !it.name.contains("(*)") }
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

    fun changeSelectedYear(id: Int) {
        _selectedYear.value = _yearList.value?.first { it.id == id }
    }


    suspend fun updateYear() {
        try {
            settingsRepository.setYear(_selectedYear.value?.id ?: -1)
            withContext(Dispatchers.Main) {
                Singleton.gradesYearId.value = _selectedYear.value
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                _errorDescription.value = e.toDescription()
            }
        }
    }

    fun getGradeSort(context: Context) {
        val settings = Settings(context)

        viewModelScope.launch {
            _gradesSortType.value = settings.sortGradesBy.first() ?: GradeSortType.BY_LESSON_NAME
        }
    }

    companion object {
        fun filterYearName(year: String): String = year.replace("(*) ", "")
    }
}
