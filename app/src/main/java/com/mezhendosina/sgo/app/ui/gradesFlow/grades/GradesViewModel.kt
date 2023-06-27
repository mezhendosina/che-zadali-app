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

package com.mezhendosina.sgo.app.ui.gradesFlow.grades

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.perf.ktx.performance
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.model.grades.GradeActionListener
import com.mezhendosina.sgo.app.model.grades.GradeSortType
import com.mezhendosina.sgo.app.model.grades.GradesRepository
import com.mezhendosina.sgo.app.utils.toDescription
import com.mezhendosina.sgo.data.SettingsDataStore
import com.mezhendosina.sgo.data.editPreference
import com.mezhendosina.sgo.data.getValue
import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton
import com.mezhendosina.sgo.data.netschool.api.grades.entities.GradesItem
import com.mezhendosina.sgo.data.netschool.api.grades.entities.gradeOptions.GradeOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class GradesViewModel(
    private val gradeServices: GradesRepository = NetSchoolSingleton.gradesRepository
) : ViewModel() {

    private val _grades = MutableLiveData<List<GradesItem>>()
    val grades: LiveData<List<GradesItem>> = _grades

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _gradeOptions = MutableLiveData<GradeOptions>()

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val gradeActionListener: GradeActionListener = {
        _grades.value = it
    }


    init {
        gradeServices.addListener(gradeActionListener)
    }

    suspend fun load(context: Context) {

        if (Singleton.grades.isNotEmpty() && Singleton.gradesRecyclerViewLoaded.value == false) {
            withContext(Dispatchers.Main) {
                _grades.value = Singleton.grades
                _isLoading.value = false
            }
            return
        }

        // start firebase performance trace
        val trace = Firebase.performance.newTrace("load_grades_trace")
        trace.start()
        withContext(Dispatchers.Main) {
            _isLoading.value = true
        }
        try {

            // gradesOption request
            val gradeOptions = gradeServices.loadGradesOptions()
            withContext(Dispatchers.Main) {
                _gradeOptions.value = gradeOptions
            }

            // save result
            withContext(Dispatchers.Main) {
                Singleton.gradesOptions.value = _gradeOptions.value
            }
            // find saved termId in response
            val currentTrimId = SettingsDataStore.CURRENT_TRIM_ID.getValue(context, "")
            val findId = _gradeOptions.value!!.TERMID.find {
                it.value == currentTrimId.first().toString()
            }

            // if termId not find save and set selected termId
            if (findId == null) SettingsDataStore.CURRENT_TRIM_ID.editPreference(
                context,
                _gradeOptions.value!!.TERMID.first { it.is_selected }.value
            )
            val sortedGradesBy =
                SettingsDataStore.SORT_GRADES_BY.getValue(context, GradeSortType.BY_LESSON_NAME)
            loadGrades(
                _gradeOptions.value!!,
                currentTrimId.first().toString(),
                sortedGradesBy.first()
            )

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                _errorMessage.value = e.toDescription()
            }
        } finally {
            withContext(Dispatchers.Main) {
                trace.stop()
                _isLoading.value = false
            }
        }
    }

    private suspend fun loadGrades(gradesOptions: GradeOptions, termID: String, sortType: Int) =
        withContext(Dispatchers.IO) {
            gradeServices.loadGrades(gradesOptions, termID, sortType)
        }

    override fun onCleared() {
        super.onCleared()

        gradeServices.removeListener(gradeActionListener)
    }
}