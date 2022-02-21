package com.che.zadali.sgoapp.ui.viewModels

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.che.zadali.sgoapp.activities.LoginActivity
import com.che.zadali.sgoapp.data.SettingsPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel : ViewModel() {
    private var _currentYear = MutableLiveData<String>()
    private var _language = MutableLiveData<String>()
    private var _theme = MutableLiveData<String>()

    var currentYear: LiveData<String> = _currentYear
    var language: LiveData<String> = _language
    var theme: LiveData<String> = _theme

    fun loadSettings(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val settings = SettingsPrefs(context)
            withContext(Dispatchers.Main){
                _currentYear.value = settings.currentYear.first()
                _language.value = settings.language.first()
                _theme.value = settings.theme.first()
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