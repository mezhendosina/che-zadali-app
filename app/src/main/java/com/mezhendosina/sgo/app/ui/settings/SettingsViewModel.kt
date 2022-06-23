package com.mezhendosina.sgo.app.ui.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.activities.LoginActivity
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.layouts.mySettingsRequest.MySettingsRequest
import com.mezhendosina.sgo.data.layouts.mySettingsRequest.UserSettings
import com.mezhendosina.sgo.data.layouts.mySettingsResponse.MySettingsResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class SettingsViewModel : ViewModel() {

    private val _mySettingsResponse = MutableLiveData<MySettingsResponse>()
    val mySettingsResponse: LiveData<MySettingsResponse> = _mySettingsResponse

    val phoneNumber = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val phoneNumberVisibility = MutableLiveData<Boolean>()

    private val controlQuestion = MutableLiveData<String>()
    private val controlAnswer = MutableLiveData<String>()

    private val file = File.createTempFile("profilePhoto", "index")

    fun getMySettings(arguments: Bundle?) {
        CoroutineScope(Dispatchers.IO).launch() {
            val settings = Singleton.requests.getMySettings(Singleton.at)
            withContext(Dispatchers.Main) {
                _mySettingsResponse.value = settings
                phoneNumber.value = settings.mobilePhone
                email.value = settings.email
                phoneNumberVisibility.value = settings.userSettings.showMobilePhone

                controlQuestion.value =
                    arguments?.getString(CONTROL_QUESTION) ?: settings.userSettings.recoveryQuestion
                controlAnswer.value =
                    arguments?.getString(CONTROL_ANSWER) ?: settings.userSettings.recoveryAnswer

            }
        }
    }

    fun loadProfilePhoto(context: Context, photoView: ImageView) {
        CoroutineScope(Dispatchers.IO).launch {
            val settings = Settings(context)
            Singleton.requests.loadPhoto(Singleton.at, settings.currentUserId.first(), file)
            withContext(Dispatchers.Main) {
                Glide.with(context).load(file).circleCrop().into(photoView)
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

    fun pickAnImage(context: Context, fragment: SettingsFragment) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        val a =
            fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val uri = result.data?.data
                    val fileString = uri?.path
                    val thumbnail = BitmapFactory.decodeFile(fileString)

                }
            }
    }

    fun sendSettings(context: Context) {

        val settingsResponse = _mySettingsResponse.value
        if (email.value != settingsResponse?.email || phoneNumber.value != settingsResponse?.mobilePhone || phoneNumberVisibility.value != settingsResponse?.userSettings?.showMobilePhone || settingsResponse?.userSettings?.recoveryQuestion != controlQuestion.value || settingsResponse?.userSettings?.recoveryAnswer != controlAnswer.value) {
            CoroutineScope(Dispatchers.IO).launch {
                val a = fromMySettingsResponseToRequest(settingsResponse, context)
                Singleton.requests.sendMySettings(
                    Singleton.at,
                    a
                )
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context.applicationContext,
                        "Изменения сохранены",
                        Toast.LENGTH_LONG
                    ).show()
                }
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

    override fun onCleared() {
        super.onCleared()
        file.delete()
    }

    fun calculateCache(context: Context): Long = context.cacheDir.calculateSizeRecursively()

    private fun File.calculateSizeRecursively(): Long {
        return walkBottomUp().fold(0L) { acc, file -> acc + file.length() }
    }

    private suspend fun fromMySettingsResponseToRequest(
        mySettingsResponse: MySettingsResponse?,
        context: Context
    ): MySettingsRequest {
        val userSettings = mySettingsResponse?.userSettings

        return MySettingsRequest(
            email.value.orEmpty(),
            phoneNumber.value.orEmpty(),
            Singleton.currentYearId,
            mySettingsResponse?.userId ?: 0,
            UserSettings(
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
            mySettingsResponse?.windowsAccount
        )
    }


    companion object {
        const val CONTROL_QUESTION = "control_question"
        const val CONTROL_ANSWER = "control_answer"
    }
}