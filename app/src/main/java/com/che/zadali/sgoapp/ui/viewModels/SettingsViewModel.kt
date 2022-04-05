package com.che.zadali.sgoapp.ui.viewModels

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.che.zadali.sgoapp.activities.LoginActivity
import com.che.zadali.sgoapp.data.services.SettingsData
import com.che.zadali.sgoapp.data.services.SettingsPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel : ViewModel() {
    private var _settingsData = MutableLiveData<SettingsData>()
    var settingsData: LiveData<SettingsData> = _settingsData


    fun loadSettings(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val s = SettingsPrefs(context).loadSettingsData()
            withContext(Dispatchers.Main) {
                _settingsData.value = s
            }
        }
    }

    fun changeTheme(theme: Int, context: Context) {
        CoroutineScope(Dispatchers.Main).launch {
           // AppCompatDelegate.setDefaultNightMode(theme)
            withContext(Dispatchers.IO) {
                SettingsPrefs(context).editTheme(theme)
            }
        }
    }

    fun logOut(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            SettingsPrefs(context).deleteAll()
            withContext(Dispatchers.Main) {
                context.startActivity(Intent(context, LoginActivity::class.java))
            }
        }
    }
}