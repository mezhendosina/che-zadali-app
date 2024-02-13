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

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.perf.ktx.performance
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.model.grades.GradeActionListener
import com.mezhendosina.sgo.app.model.grades.GradeSortType
import com.mezhendosina.sgo.app.model.grades.GradesRepositoryInterface
import com.mezhendosina.sgo.app.uiEntities.checkItem
import com.mezhendosina.sgo.app.utils.LoadStates
import com.mezhendosina.sgo.app.utils.toDescription
import com.mezhendosina.sgo.data.SettingsDataStore
import com.mezhendosina.sgo.data.netschool.api.grades.entities.GradesItem
import com.mezhendosina.sgo.data.netschool.api.grades.entities.gradeOptions.GradeOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GradesViewModel
    @Inject
    constructor(
        private val gradeServices: GradesRepositoryInterface,
        private val settingsDataStore: SettingsDataStore,
    ) : ViewModel() {
        private val _grades = MutableLiveData<List<GradesItem>>()
        val grades: LiveData<List<GradesItem>> = _grades

        private val _gradeOptions = MutableLiveData<GradeOptions>()

        private val _errorMessage = MutableLiveData<String>()
        val errorMessage: LiveData<String> = _errorMessage

        private val gradeActionListener: GradeActionListener = {
            _grades.value = it
        }

        var gradeAdapter: GradeAdapter? = null

        init {
            gradeServices.addListener(gradeActionListener)
        }

        fun setAdapter(onClickListener: OnGradeClickListener) {
            gradeAdapter = GradeAdapter(onClickListener)
        }

        fun setLesson(lesson: GradesItem) = gradeServices.setSelectedGradesItem(lesson)

        suspend fun load() {
            if (Singleton.grades.isNotEmpty() && Singleton.gradesRecyclerViewLoaded.value == false) {
                withContext(Dispatchers.Main) {
                    _grades.value = Singleton.grades
                    Singleton.updateGradeState.value = LoadStates.FINISHED
                }
                return
            } else {
                withContext(Dispatchers.Main) {
                    _grades.value = emptyList()
                }
            }

            // start firebase performance trace
            val trace = Firebase.performance.newTrace("load_grades_trace")
            trace.start()

            try {
                // gradesOption request
                val gradeOptions = gradeServices.loadGradesOptions()
                withContext(Dispatchers.Main) {
                    _gradeOptions.value = gradeOptions
                }

                // find saved termId in response
                val currentTrimId = settingsDataStore.getValue(SettingsDataStore.TRIM_ID).first() ?: -1
                val findId =
                    _gradeOptions.value!!.TERMID.find {
                        it.value == currentTrimId.toString()
                    }

                // if termId not find save and set selected termId
                if (findId == null) {
                    settingsDataStore.setValue(
                        SettingsDataStore.TRIM_ID,
                        _gradeOptions.value!!.TERMID.first { it.is_selected }.value.toInt(),
                    )
                }
                val sortedGradesBy =
                    settingsDataStore.getValue(SettingsDataStore.SORT_GRADES_BY).first()
                        ?: GradeSortType.BY_LESSON_NAME
                loadGrades(
                    _gradeOptions.value!!,
                    currentTrimId.toString(),
                    sortedGradesBy,
                )
                // Save terms into Singleton
                val trims = _gradeOptions.value!!.getTerms()
                val checkSelectedTrim = trims.checkItem(currentTrimId)
                withContext(Dispatchers.Main) {
                    Singleton.gradesTerms.value = checkSelectedTrim
                    Singleton.updateGradeState.value = LoadStates.FINISHED
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e(null, e.stackTraceToString())
                    _errorMessage.value = e.toDescription()
                    Singleton.updateGradeState.value = LoadStates.ERROR
                }
            } finally {
                withContext(Dispatchers.Main) {
                    trace.stop()
                }
            }
        }

        private suspend fun loadGrades(
            gradesOptions: GradeOptions,
            termID: String,
            sortType: Int,
        ) = withContext(Dispatchers.IO) {
            gradeServices.loadGrades(gradesOptions, termID, sortType)
        }

        override fun onCleared() {
            super.onCleared()

            gradeServices.removeListener(gradeActionListener)
        }
    }
