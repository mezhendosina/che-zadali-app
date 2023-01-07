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

package com.mezhendosina.sgo.app.ui.changePhone

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.model.settings.SettingsRepository
import com.mezhendosina.sgo.app.toDescription
import com.mezhendosina.sgo.app.toLiveData
import com.mezhendosina.sgo.data.requests.sgo.settings.entities.MySettingsResponseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChangePhoneViewModel(
    private val settingsRepository: SettingsRepository = Singleton.settingsRepository
) : ViewModel() {

    private val _errorDescription = MutableLiveData<String>()
    val errorDescription = _errorDescription.toLiveData()

    private val _mySettings = MutableLiveData<MySettingsResponseEntity>()

    suspend fun changePhone(changedPhone: String) {
        withContext(Dispatchers.IO) {
            try {
                if (_mySettings.value != null) {
                    settingsRepository.sendSettings(
                        _mySettings.value!!.toRequestEntity().changeMobilePhone(changedPhone)
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorDescription.value = e.toDescription()
                }
            }
        }
    }

    suspend fun getSettings() {
        if (Singleton.mySettings.value == null) {
            val mySettings = settingsRepository.getMySettings()
            withContext(Dispatchers.Main) {
                _mySettings.value = mySettings
            }
        } else withContext(Dispatchers.Main) {
            _mySettings.value = Singleton.mySettings.value
        }
    }
}