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

package com.mezhendosina.sgo.app.activities

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.app.utils.toDescription
import com.mezhendosina.sgo.data.SettingsDataStore
import com.mezhendosina.sgo.data.getValue
import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton
import com.mezhendosina.sgo.data.netschool.repo.LoginRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val loginRepository: LoginRepository = NetSchoolSingleton.loginRepository
) : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage


    suspend fun login(context: Context) {
        try {
            val login = SettingsDataStore.LOGIN.getValue(context, "").first()
            if (login.isEmpty()) {
                loginRepository.gosuslugiLogin(
                    context,
                    SettingsDataStore.ESIA_LOGIN_STATE.getValue(context, "").first(),
                    SettingsDataStore.ESIA_USER_ID.getValue(context, "").first()
                )
            } else {
                loginRepository.login(
                    context,
                    SettingsDataStore.LOGIN.getValue(context, "").first(),
                    SettingsDataStore.PASSWORD.getValue(context, "").first(),
                    SettingsDataStore.SCHOOL_ID.getValue(context, -1).first(),
                    false
                )
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                _errorMessage.value = e.toDescription()
            }
        }
    }

    fun logout() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                loginRepository.logout()
            } catch (_: Exception) {
            }
        }
    }
}