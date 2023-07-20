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

package com.mezhendosina.sgo.app.ui.loginFlow.login

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.activities.MainActivity
import com.mezhendosina.sgo.app.uiEntities.SchoolUiEntity
import com.mezhendosina.sgo.app.utils.toDescription
import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton
import com.mezhendosina.sgo.data.netschool.api.login.entities.toUiEntity
import com.mezhendosina.sgo.data.netschool.base.toMD5
import com.mezhendosina.sgo.data.netschool.repo.LoginRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val loginRepository: LoginRepository = NetSchoolSingleton.loginRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage


    fun login(
        context: Context,
        schoolId: Int,
        login: String,
        password: String,
        navController: NavController
    ) {
        _isLoading.value = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val passwordHash = password.toMD5()
                val school = findSchool(schoolId)
                loginRepository.login(
                    context,
                    login,
                    passwordHash,
                    school.id,
                    onOneUser = {
                        val intent = Intent(context, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(context, intent, null)
                    },
                    onMoreUser = {
                        Singleton.users = it.toUiEntity()
                        navController.navigate(R.id.action_loginFragment_to_chooseUserIdFragment)
                    }
                )
                loginRepository.logout()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = e.toDescription()
                }
            } finally {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }
        }
    }


    fun findSchool(schoolId: Int): SchoolUiEntity {
        return NetSchoolSingleton.schools.first { it.id == schoolId }
    }
}

