package com.mezhendosina.sgo.app.model.login

import com.mezhendosina.sgo.data.requests.sgo.login.entities.GetDataResponseEntity
import com.mezhendosina.sgo.data.requests.sgo.login.entities.LoginResponseEntity
import com.mezhendosina.sgo.data.requests.sgo.login.entities.LogoutRequestEntity
import com.mezhendosina.sgo.data.requests.sgo.login.entities.StudentResponseEntity

interface LoginSource {

    suspend fun loginData()

    suspend fun getData(): GetDataResponseEntity

    suspend fun login(loginEntity: LoginEntity): LoginResponseEntity

    suspend fun getStudents(): List<StudentResponseEntity>?

    suspend fun logout(logoutRequestEntity: LogoutRequestEntity)
}