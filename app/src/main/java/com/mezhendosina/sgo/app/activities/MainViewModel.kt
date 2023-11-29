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
import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton
import com.mezhendosina.sgo.data.netschool.repo.LoginRepository
import com.mezhendosina.sgo.data.netschool.repo.LoginRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class MainViewModel
@Inject constructor(
    private val loginRepository: LoginRepositoryInterface,
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage


    suspend fun login() {
        try {
            val login = settingsDataStore.getValue(SettingsDataStore.LOGIN).first() ?: ""
            if (login.isEmpty()) {
                loginRepository.gosuslugiLogin()
            } else {
                loginRepository.login(firstLogin = false)
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