package com.mezhendosina.sgo.app.model.login

import com.mezhendosina.sgo.data.requests.login.entities.GetDataResponseEntity
import com.mezhendosina.sgo.data.requests.login.entities.LoginResponseEntity
import com.mezhendosina.sgo.data.requests.login.entities.LogoutRequestEntity

interface LoginSource {

    suspend fun loginData()

    suspend fun getData(): GetDataResponseEntity

    suspend fun login(loginEntity: LoginEntity): LoginResponseEntity

    suspend fun logout(logoutRequestEntity: LogoutRequestEntity)
}