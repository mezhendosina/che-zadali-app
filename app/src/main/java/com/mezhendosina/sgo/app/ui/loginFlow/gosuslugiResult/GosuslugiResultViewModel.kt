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

package com.mezhendosina.sgo.app.ui.loginFlow.gosuslugiResult

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.app.utils.toDescription
import com.mezhendosina.sgo.app.utils.toLiveData
import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton
import com.mezhendosina.sgo.data.netschool.repo.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class GosuslugiResultViewModel
@Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _loggedIn = MutableLiveData<Boolean?>(null)
    val loggedIn = _loggedIn.toLiveData()

    private val _error = MutableLiveData<String>()
    val error = _error.toLiveData()

    suspend fun auth() {
        try {
            loginRepository.gosuslugiLogin(true)

            withContext(Dispatchers.Main) { _loggedIn.value = true }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                _loggedIn.value = false
                _error.value = e.toDescription()
            }
        }
    }
}
