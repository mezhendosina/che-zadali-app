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

package com.mezhendosina.sgo.app.ui.journalItem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.model.journal.JournalRepository
import com.mezhendosina.sgo.app.model.journal.entities.DiaryUiEntity
import com.mezhendosina.sgo.app.utils.toDescription
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.WeekStartEndEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class JournalItemViewModel(
    private val journalRepository: JournalRepository = Singleton.journalRepository
) : ViewModel() {

    private val _week = MutableLiveData<DiaryUiEntity>()
    val week: LiveData<DiaryUiEntity> = _week

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    suspend fun getWeek(weekStart: String?, weekEnd: String?) {
        val settings = Settings(Singleton.getContext())

        if (Singleton.gradesRecyclerViewLoaded.value == false) {
            withContext(Dispatchers.Main) {
                _week.value = Singleton.currentDiaryUiEntity.value
            }
            return
        }
        withContext(Dispatchers.Main) {
            _isLoading.value = true
            _errorMessage.value = ""
        }
        withContext(Dispatchers.IO) {
            try {
                val a = if (Singleton.currentDiaryUiEntity.value?.weekStart == weekStart) {
                    Singleton.currentDiaryUiEntity.value!!
                } else {
                    val getWeek = journalRepository.getWeek(
                        settings.currentUserId.first(),
                        WeekStartEndEntity(weekStart!!, weekEnd!!),
                        Singleton.currentYearId.value ?: 0
                    )
                    Singleton.loadedDiaryUiEntity.add(getWeek)
                    getWeek
                }

                withContext(Dispatchers.Main) {
                    _week.value = a
                }
            } catch (e: Exception) {
                val errorDescription = e.toDescription()
                withContext(Dispatchers.Main) {
                    _errorMessage.value = errorDescription
                }
            } finally {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }
        }
    }
}