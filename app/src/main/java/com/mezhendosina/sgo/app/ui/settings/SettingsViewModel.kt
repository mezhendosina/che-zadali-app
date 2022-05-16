package com.mezhendosina.sgo.app.ui.settings

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.app.activities.LoginActivity
import com.mezhendosina.sgo.data.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel : ViewModel() {

    private val _currentTheme = MutableLiveData<Int>()
    val currentTheme: LiveData<Int> = _currentTheme

    fun getCurrentTheme(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val settings = Settings(context)
            withContext(Dispatchers.Main){
//                _currentTheme.value = settings.theme.first()
            }
        }
    }

    fun logout(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            Settings(context).logout()
            withContext(Dispatchers.Main){
                startActivity(context, Intent(context, LoginActivity::class.java), null)
            }
        }
    }

}