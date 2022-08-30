package com.mezhendosina.sgo.app.model.login

import android.content.Context
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.model.settings.SettingsSource
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.requests.login.entities.LogoutRequestEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginRepository(
    private val loginSource: LoginSource,
    private val settingsSource: SettingsSource
) {

    var at: String? = null

    suspend fun login(context: Context, schoolId: Int, login: String, password: String) {
        loginSource.loginData()
        val getData = loginSource.getData()
        val loginEntity = LoginEntity(
            2,
            1,
            -1,
            1,
            2,
            schoolId,
            login,
            password,
            getData.lt,
            getData.salt,
            getData.ver
        )
        val loginRequest = loginSource.login(loginEntity)
        at = loginRequest.at
        withContext(Dispatchers.IO) {
            val settings = Settings(context)
            settings.saveALl(loginEntity)
            settings.setCurrentUserId(loginRequest.accountInfo.user.id)
            Singleton.at = loginRequest.at
            val yearsID = settingsSource.getYearList().first { !it.name.contains("(*)") }.id
            Singleton.currentYearId = yearsID
        }
        logout()
    }

    suspend fun logout() = loginSource.logout(LogoutRequestEntity(at.toString()))

}