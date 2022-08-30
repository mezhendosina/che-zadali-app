package com.mezhendosina.sgo.app.activities

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.model.login.LoginEntity
import com.mezhendosina.sgo.app.model.login.LoginSource
import com.mezhendosina.sgo.app.toDescription
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.SettingsLoginData
import com.mezhendosina.sgo.data.requests.login.entities.GetDataResponseEntity
import com.mezhendosina.sgo.data.requests.login.entities.LogoutRequestEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val loginSource: LoginSource = Singleton.loginSource
) : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage


    suspend fun login(context: Context) {
            try {
                val settings = Settings(context)
                val settingsLoginData = settings.getLoginData()
                loginSource.loginData()
                val getData = loginSource.getData()

                val loginEntity = settingsLoginData.toLoginEntity(getData)
                val login = loginSource.login(loginEntity)
                Singleton.at = login.at

            } catch (e: Exception) {
                withContext(Dispatchers.Main){
                    _errorMessage.value = e.toDescription()
                }
            }
    }

    fun logout() {
        CoroutineScope(Dispatchers.IO).launch {
            loginSource.logout(LogoutRequestEntity(Singleton.at))
        }
    }

    private fun SettingsLoginData.toLoginEntity(
        getDataResponseEntity: GetDataResponseEntity
    ): LoginEntity = LoginEntity(
        this.cn,
        this.sid,
        this.pid,
        this.cn,
        this.sft,
        this.scid,
        this.UN,
        this.PW,
        getDataResponseEntity.lt,
        getDataResponseEntity.salt,
        getDataResponseEntity.ver
    )
}