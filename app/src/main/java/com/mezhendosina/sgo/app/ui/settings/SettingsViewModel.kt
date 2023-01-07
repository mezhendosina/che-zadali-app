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

package com.mezhendosina.sgo.app.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessaging
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.activities.LoginActivity
import com.mezhendosina.sgo.app.model.settings.SettingsRepository
import com.mezhendosina.sgo.app.toDescription
import com.mezhendosina.sgo.app.toLiveData
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.requests.notifications.entities.NotificationUserEntity
import com.mezhendosina.sgo.data.requests.sgo.settings.entities.MySettingsResponseEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class SettingsViewModel(
    private val settingsRepository: SettingsRepository = Singleton.settingsRepository
) : ViewModel() {

    private val _mySettingsResponseEntity = MutableLiveData<MySettingsResponseEntity>()
    val mySettingsResponseEntity: LiveData<MySettingsResponseEntity> = _mySettingsResponseEntity

    val phoneNumber = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val phoneNumberVisibility = MutableLiveData<Boolean>()

    private val controlQuestion = MutableLiveData<String>()
    private val controlAnswer = MutableLiveData<String>()

    private val _enableGradeNotifications = MutableLiveData<Boolean>()
    val enableGradeNotifications: LiveData<Boolean> = _enableGradeNotifications

    private val _gradesNotificationsLoading = MutableLiveData<Boolean>(false)
    val gradesNotificationsLoading = _gradesNotificationsLoading.toLiveData()

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _loading = MutableLiveData<Boolean>(false)
    val loading = _loading.toLiveData()

    var firebaseToken: String? = null

    suspend fun getMySettings(arguments: Bundle?) {
        try {
            withContext(Dispatchers.Main) {
                _loading.value = true
            }
            val settingsResponse = settingsRepository.getMySettings()
//            val settings = Settings(Singleton.getContext())
//            isGradesNotifySignIn(settings)
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
                _errorMessage.value = e.localizedMessage
            }
        } finally {
            withContext(Dispatchers.Main) {
                _loading.value = false
            }
        }
    }

    fun loadProfilePhoto(context: Context, photoView: ImageView) {
        val dir = context.filesDir
        val photoFile = File(dir, "profilePhoto")
        val isExist = photoFile.createNewFile()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val settings = Settings(context)

                settingsRepository.loadProfilePhoto(
                    settings.currentUserId.first(), photoFile, isExist
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

    fun changeTheme(selectedThemeId: Int, context: Context) {
        val themeId = when (selectedThemeId) {
            R.id.light_theme -> AppCompatDelegate.MODE_NIGHT_NO
            R.id.dark_theme -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

        CoroutineScope(Dispatchers.IO).launch {
            Settings(context).setTheme(themeId)
        }

        AppCompatDelegate.setDefaultNightMode(themeId)
    }

    suspend fun changeGradeNotifications() {
        val settings = Settings(Singleton.getContext())
        withContext(Dispatchers.Main) {
            _gradesNotificationsLoading.value = true
        }
        try {
            val token =
                if (firebaseToken != null) firebaseToken
                else {
                    isGradesNotifySignIn(settings)
                    firebaseToken
                }
            val loginData = settings.getLoginData()
            val userId = settings.currentUserId.first()
            val user = NotificationUserEntity(
                userId,
                token ?: "",
                loginData.UN,
                loginData.PW,
                settings.regionUrl.first()?.dropLast(1) ?: "",
                loginData.schoolId,
                true
            )
            if (!_enableGradeNotifications.value!!) {
                settingsRepository.registerGradesNotifications(user)
            } else {
                settingsRepository.unregisterGradesNotifications(userId, token ?: "")
            }
            withContext(Dispatchers.Main) {
                _enableGradeNotifications.value = !_enableGradeNotifications.value!!
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                _errorMessage.value = e.toDescription()
                _enableGradeNotifications.value = _enableGradeNotifications.value!!
            }
        } finally {
            withContext(Dispatchers.Main) {
                _gradesNotificationsLoading.value = false
            }
        }
    }

    fun logout(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            Settings(context).logout()
            withContext(Dispatchers.Main) {
                val intent = Intent(context, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(context, intent, null)
            }
        }
    }

    private suspend fun isGradesNotifySignIn(settings: Settings) {
        if (firebaseToken == null) {
            withContext(Dispatchers.Main) {
                _gradesNotificationsLoading.value = true
            }
            FirebaseMessaging.getInstance().token.addOnSuccessListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val isExist = settingsRepository.isGradesNotifyUserExist(
                        settings.currentUserId.first(),
                        it
                    )
                    withContext(Dispatchers.Main) {
                        _enableGradeNotifications.value = isExist
                        firebaseToken = it
                        _gradesNotificationsLoading.value = false
                    }
                }
            }
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    _gradesNotificationsLoading.value = true
                }
                val isExist = settingsRepository.isGradesNotifyUserExist(
                    settings.currentUserId.first(),
                    firebaseToken ?: ""
                )
                withContext(Dispatchers.Main) {
                    _enableGradeNotifications.value = isExist
                    _gradesNotificationsLoading.value = false
                }
            }
        }
    }

    companion object {
        const val CONTROL_QUESTION = "control_question"
        const val CONTROL_ANSWER = "control_answer"
    }
}