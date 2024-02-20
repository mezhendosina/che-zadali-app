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

package com.mezhendosina.sgo.app.ui.settingsFlow

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.mezhendosina.sgo.app.utils.toDescription
import com.mezhendosina.sgo.app.utils.toLiveData
import com.mezhendosina.sgo.data.SettingsDataStore
import com.mezhendosina.sgo.data.netschool.api.settings.entities.MySettingsResponseEntity
import com.mezhendosina.sgo.data.netschool.repo.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
    @Inject
    constructor(
        private val settingsRepository: SettingsRepository,
        private val settingsDataStore: SettingsDataStore,
    ) : ViewModel() {
        private val _mySettingsResponseEntity = MutableLiveData<MySettingsResponseEntity>()
        val mySettingsResponseEntity: LiveData<MySettingsResponseEntity> = _mySettingsResponseEntity

        val phoneNumber = MutableLiveData<String>()
        val email = MutableLiveData<String>()
        val phoneNumberVisibility = MutableLiveData<Boolean>()

        private val controlQuestion = MutableLiveData<String>()
        private val controlAnswer = MutableLiveData<String>()

        private val _errorMessage = MutableLiveData<String>()
        val errorMessage: LiveData<String> = _errorMessage

        private val _loading = MutableLiveData<Boolean>(false)
        val loading = _loading.toLiveData()

        suspend fun getMySettings(arguments: Bundle?) {
            try {
                withContext(Dispatchers.Main) {
                    _loading.value = true
                }
                val settingsResponse = settingsRepository.getMySettings()
                withContext(Dispatchers.Main) {
                    _mySettingsResponseEntity.value = settingsResponse
                    phoneNumber.value = settingsResponse.mobilePhone ?: ""
                    email.value = settingsResponse.email ?: ""
                    phoneNumberVisibility.value = settingsResponse.userSettings.showMobilePhone

                    controlQuestion.value = arguments?.getString(CONTROL_QUESTION)
                        ?: settingsResponse.userSettings.recoveryQuestion.toString()
                    controlAnswer.value = arguments?.getString(CONTROL_ANSWER)
                        ?: settingsResponse.userSettings.recoveryAnswer.toString()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = e.toDescription()
                }
            } finally {
                withContext(Dispatchers.Main) {
                    _loading.value = false
                }
            }
        }

        fun loadProfilePhoto(
            context: Context,
            photoView: ImageView,
        ) {
            val dir = context.filesDir
            val photoFile = File(dir, "profilePhoto")
            val isExist = photoFile.createNewFile()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    settingsRepository.loadProfilePhoto(
                        settingsDataStore.getValue(SettingsDataStore.CURRENT_USER_ID).first() ?: -1,
                        photoFile,
                    )

                    withContext(Dispatchers.Main) {
                        Glide.with(context).load(photoFile).circleCrop().into(photoView)
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        _errorMessage.value = e.toDescription()
                    }
                }
            }
        }

        suspend fun logout() {
            settingsDataStore.logout()
        }

        companion object {
            const val CONTROL_QUESTION = "control_question"
            const val CONTROL_ANSWER = "control_answer"
        }
    }
