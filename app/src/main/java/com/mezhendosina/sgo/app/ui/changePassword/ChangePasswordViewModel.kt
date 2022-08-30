package com.mezhendosina.sgo.app.ui.changePassword

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.model.settings.SettingsRepository
import com.mezhendosina.sgo.app.toDescription
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.requests.settings.entities.ChangePasswordEntity
import com.mezhendosina.sgo.data.toMD5
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChangePasswordViewModel(
    private val settingsRepository: SettingsRepository = Singleton.settingsRepository
) : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun changePassword(oldPassword: String, newPassword: String, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val settings = Settings(context)
                val password = newPassword.toMD5()
                settingsRepository.changePassword(
                    settings.currentUserId.first(),
                    ChangePasswordEntity(oldPassword, password)
                )
                settings.changePassword(password)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = e.toDescription()
                }
            }
        }
    }
}