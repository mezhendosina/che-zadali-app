package com.mezhendosina.sgo.app.activities

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.model.login.LoginRepository
import com.mezhendosina.sgo.app.toDescription
import com.mezhendosina.sgo.data.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val loginRepository: LoginRepository = Singleton.loginRepository
) : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage


    suspend fun login(context: Context) {
        try {
            val settings = Settings(context)
            val settingsLoginData = settings.getLoginData()
            loginRepository.login(
                context,
                settingsLoginData.scid,
                settingsLoginData.UN,
                settingsLoginData.PW,
                false
            )

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                _errorMessage.value = e.toDescription()
            }
        }
    }

    fun logout() {
        CoroutineScope(Dispatchers.IO).launch {
            loginRepository.logout()
        }
    }
}