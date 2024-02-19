package com.mezhendosina.sgo.data.netschoolEsia.api.login

import com.mezhendosina.sgo.data.netschool.api.login.entities.accountInfo.AccountInfoResponseEntity
import com.mezhendosina.sgo.data.requests.sgo.login.entities.LoginResponseEntity
import okhttp3.ResponseBody
import retrofit2.Response

interface LoginSource {
    suspend fun crossLogin(): Response<ResponseBody>

    suspend fun getGosuslugiAccountInfo(loginState: String): AccountInfoResponseEntity

    suspend fun gosuslugiLogin(
        loginState: String,
        userId: String,
    ): LoginResponseEntity
}
