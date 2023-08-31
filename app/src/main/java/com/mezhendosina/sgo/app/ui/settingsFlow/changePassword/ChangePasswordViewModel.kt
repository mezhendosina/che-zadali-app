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

package com.mezhendosina.sgo.app.ui.settingsFlow.changePassword

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.app.utils.toDescription
import com.mezhendosina.sgo.data.SettingsDataStore
import com.mezhendosina.sgo.data.editPreference
import com.mezhendosina.sgo.data.getValue
import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton
import com.mezhendosina.sgo.data.netschool.api.settings.entities.ChangePasswordEntity
import com.mezhendosina.sgo.data.netschool.base.toMD5
import com.mezhendosina.sgo.data.netschool.repo.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChangePasswordViewModel(
    private val settingsRepository: SettingsRepository = NetSchoolSingleton.settingsRepository
) : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun changePassword(oldPassword: String, newPassword: String, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val password = newPassword.toMD5()
                settingsRepository.changePassword(
                    SettingsDataStore.CURRENT_USER_ID.getValue(context, -1).first(),
                    ChangePasswordEntity(oldPassword, password)
                )
                SettingsDataStore.PASSWORD.editPreference(context, password)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = e.toDescription()
                }
            }
        }
    }
}