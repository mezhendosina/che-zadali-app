package com.mezhendosina.sgo.app.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.activities.LoginActivity
import com.mezhendosina.sgo.app.model.settings.SettingsRepository
import com.mezhendosina.sgo.app.toDescription
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.requests.sgo.settings.entities.MySettingsRequestEntity
import com.mezhendosina.sgo.data.requests.sgo.settings.entities.MySettingsResponseEntity
import com.mezhendosina.sgo.data.requests.sgo.settings.entities.UserSettingsEntity
import com.mezhendosina.sgo.data.requests.sgo.settings.entities.YearListResponseEntity
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

    private val _years = MutableLiveData<List<YearListResponseEntity>>()
    val years: LiveData<List<YearListResponseEntity>> = _years

    private val selectedYear = MutableLiveData<Int>()

    val phoneNumber = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val phoneNumberVisibility = MutableLiveData<Boolean>()

    private val controlQuestion = MutableLiveData<String>()
    private val controlAnswer = MutableLiveData<String>()

    val showTime = MutableLiveData<Boolean>()
    val showNumber = MutableLiveData<Boolean>()

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage


    fun getMySettings(arguments: Bundle?) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val settings = settingsRepository.getMySettings()
                val yearList = settingsRepository.getYears()
                withContext(Dispatchers.Main) {
                    _mySettingsResponseEntity.value = settings
                    phoneNumber.value = settings.mobilePhone
                    email.value = settings.email
                    phoneNumberVisibility.value = settings.userSettings.showMobilePhone

                    controlQuestion.value =
                        arguments?.getString(CONTROL_QUESTION)
                            ?: settings.userSettings.recoveryQuestion
                    controlAnswer.value =
                        arguments?.getString(CONTROL_ANSWER) ?: settings.userSettings.recoveryAnswer
                    _years.value = yearList
                    selectedYear.value = yearList.find { !it.name.contains("(*)") }?.id
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = e.toDescription()
                }
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
                    settings.currentUserId.first(),
                    photoFile,
                    isExist
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

    fun changePhoto(context: Context, data: Intent) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val file = File(data.data.toString())
                settingsRepository.changePhoto(
                    file,
                    Settings(context).currentUserId.first()
                )
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = e.toDescription()
                }
            }
        }
    }

    fun sendSettings(context: Context) {
        val settingsResponse = _mySettingsResponseEntity.value
        if (selectedYear.value != Singleton.currentYearId.value) Singleton.updateDiary.value = true
        if (email.value != settingsResponse?.email || phoneNumber.value != settingsResponse?.mobilePhone || phoneNumberVisibility.value != settingsResponse?.userSettings?.showMobilePhone || settingsResponse?.userSettings?.recoveryQuestion != controlQuestion.value || settingsResponse?.userSettings?.recoveryAnswer != controlAnswer.value) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val a = settingsResponse.toRequestEntity(context)
                    settingsRepository.sendSettings(a)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context.applicationContext,
                            "Изменения сохранены",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        _errorMessage.value = e.toDescription()
                    }
                }
            }
        }
    }

    fun changeLessonTime(b: Boolean) {
        viewModelScope.launch {
            Settings(Singleton.getContext()).changeLessonTime(b)
        }
    }

    fun changeLessonNumber(b: Boolean) {
        viewModelScope.launch {
            Settings(Singleton.getContext()).changeLessonNumber(b)
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


    fun calculateCache(context: Context): Long = context.cacheDir.calculateSizeRecursively()

    private fun File.calculateSizeRecursively(): Long {
        return walkBottomUp().fold(0L) { acc, file -> acc + file.length() }
    }

    private suspend fun MySettingsResponseEntity?.toRequestEntity(context: Context): MySettingsRequestEntity {
        val userSettings = this?.userSettings

        return MySettingsRequestEntity(
            email.value.orEmpty(),
            Regex("[^0-9]").replace(phoneNumber.value.orEmpty(), ""),
            Singleton.currentYearId.value ?: 0,
            this?.userId ?: 0,
            UserSettingsEntity(
                phoneNumberVisibility.value ?: false,
                userSettings?.defaultDesktop ?: 0,
                userSettings?.favoriteReports ?: emptyList(),
                userSettings?.language ?: "ru",
                userSettings?.passwordExpired ?: 0,
                controlAnswer.value ?: "",
                controlQuestion.value ?: "0",
                userSettings?.showNetSchoolApp ?: true,
                userSettings?.theme ?: 0,
                userSettings?.userId ?: Settings(context).currentUserId.first()
            ),
            this?.windowsAccount
        )
    }

    companion object {
        const val CONTROL_QUESTION = "control_question"
        const val CONTROL_ANSWER = "control_answer"
        const val YEAR = "year"
    }
}