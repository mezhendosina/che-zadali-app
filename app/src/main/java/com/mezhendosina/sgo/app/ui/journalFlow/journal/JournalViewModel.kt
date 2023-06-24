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

package com.mezhendosina.sgo.app.ui.journalFlow.journal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.data.WeekStartEndEntity
import com.mezhendosina.sgo.data.getWeeksList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class JournalViewModel : ViewModel() {
    private val _weeks = MutableLiveData<List<WeekStartEndEntity>>()
    val weeks: LiveData<List<WeekStartEndEntity>> = _weeks


    suspend fun loadWeeks() {
        if (Singleton.weeks.isNotEmpty()) {
            withContext(Dispatchers.Main) {
                _weeks.value = Singleton.weeks
            }
        } else {
            val a = getWeeksList()
            withContext(Dispatchers.Main) {
                _weeks.value = a
            }

        }
    }

}