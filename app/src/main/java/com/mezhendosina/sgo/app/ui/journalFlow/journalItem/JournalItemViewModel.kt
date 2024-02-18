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

package com.mezhendosina.sgo.app.ui.journalFlow.journalItem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.model.journal.JournalRepository
import com.mezhendosina.sgo.app.model.journal.entities.DiaryUiEntity
import com.mezhendosina.sgo.app.ui.journalFlow.journalItem.adapters.DiaryAdapter
import com.mezhendosina.sgo.app.ui.journalFlow.journalItem.adapters.OnHomeworkClickListener
import com.mezhendosina.sgo.app.ui.journalFlow.journalItem.adapters.PastMandatoryAdapter
import com.mezhendosina.sgo.app.ui.journalFlow.journalItem.adapters.PastMandatoryClickListener
import com.mezhendosina.sgo.app.utils.toDescription
import com.mezhendosina.sgo.data.SettingsDataStore
import com.mezhendosina.sgo.data.WeekStartEndEntity
import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class JournalItemViewModel
    @Inject
    constructor(
        private val journalRepository: JournalRepository,
        private val settingsDataStore: SettingsDataStore,
    ) : ViewModel() {
        private val _week = MutableLiveData<DiaryUiEntity>()
        val week: LiveData<DiaryUiEntity> = _week

        private val _isLoading = MutableLiveData(false)
        val isLoading: LiveData<Boolean> = _isLoading

        private val _errorMessage = MutableLiveData<String>()
        val errorMessage: LiveData<String> = _errorMessage

        var pastMandatoryAdapter: PastMandatoryAdapter? = null
        var diaryAdapter: DiaryAdapter? = null

        fun setupAdapters(
            onPastMandatoryClickListener: PastMandatoryClickListener,
            onHomeworkClickListener: OnHomeworkClickListener,
        ) {
            setupPastMandatoryAdapter(onPastMandatoryClickListener)
            setupDiaryAdapter(onHomeworkClickListener)
        }

        private fun setupPastMandatoryAdapter(onClick: PastMandatoryClickListener) {
            if (pastMandatoryAdapter == null) {
                pastMandatoryAdapter = PastMandatoryAdapter(onClick)
                pastMandatoryAdapter?.items = _week.value?.pastMandatory ?: listOf()
            }
        }

        private fun setupDiaryAdapter(onClick: OnHomeworkClickListener) {
            if (diaryAdapter == null) {
                diaryAdapter = DiaryAdapter(onClick)
                diaryAdapter?.weekDays = _week.value?.weekDays ?: listOf()
            }
        }

        suspend fun getWeek(
            weekStart: String?,
            weekEnd: String?,
        ) {
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
                    val a =
                        if (Singleton.currentDiaryUiEntity.value?.weekStart == weekStart) {
                            Singleton.currentDiaryUiEntity.value!!
                        } else {
                            val currentUserId =
                                settingsDataStore.getValue(SettingsDataStore.CURRENT_USER_ID).first()
                                    ?: -1
                            val getWeek =
                                journalRepository.getWeek(
                                    currentUserId,
                                    WeekStartEndEntity(weekStart!!, weekEnd!!),
                                    NetSchoolSingleton.journalYearId.value ?: 0,
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
