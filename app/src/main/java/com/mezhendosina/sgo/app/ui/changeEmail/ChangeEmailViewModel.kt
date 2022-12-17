package com.mezhendosina.sgo.app.ui.changeEmail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.model.settings.SettingsRepository
import com.mezhendosina.sgo.app.toDescription
import com.mezhendosina.sgo.app.toLiveData
import com.mezhendosina.sgo.data.requests.sgo.settings.entities.MySettingsResponseEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChangeEmailViewModel(
    private val settingsRepository: SettingsRepository = Singleton.settingsRepository
) : ViewModel() {

    private val _errorDescription = MutableLiveData<String>()
    val errorDescription = _errorDescription.toLiveData()

    private val _mySettings = MutableLiveData<MySettingsResponseEntity>()

    fun changeEmail(changedEmail: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (_mySettings.value != null) {
                    settingsRepository.sendSettings(
                        _mySettings.value!!.toRequestEntity().changeEmail(changedEmail)
                    )

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorDescription.value = e.toDescription()
                }
            }
        }
    }

    suspend fun getSettings() {
        if (Singleton.mySettings.value == null) {
            val mySettings = settingsRepository.getMySettings()
            withContext(Dispatchers.Main) {
                _mySettings.value = mySettings
            }
        } else withContext(Dispatchers.Main) {
            _mySettings.value = Singleton.mySettings.value
        }
    }

}