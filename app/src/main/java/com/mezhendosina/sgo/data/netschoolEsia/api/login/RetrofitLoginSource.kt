package com.mezhendosina.sgo.data.netschoolEsia.api.login

import com.mezhendosina.sgo.data.netschool.api.login.entities.accountInfo.AccountInfoResponseEntity
import com.mezhendosina.sgo.data.netschool.base.BaseRetrofitSource
import com.mezhendosina.sgo.data.netschool.base.RetrofitConfig
import com.mezhendosina.sgo.data.requests.sgo.login.entities.LoginResponseEntity
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitLoginSource
    @Inject
    constructor(
        config: RetrofitConfig,
    ) : BaseRetrofitSource(config), LoginSource {
        private val loginApi = retrofit.create(LoginApi::class.java)

        override suspend fun crossLogin(): Response<ResponseBody> =
            wrapRetrofitExceptions(false) {
                loginApi.crossLogin()
            }

        override suspend fun getGosuslugiAccountInfo(loginState: String): AccountInfoResponseEntity =
            wrapRetrofitExceptions(false) {
                loginApi.getAccountInfo(loginState)
            }

        override suspend fun gosuslugiLogin(
            loginState: String,
            userId: String,
        ): LoginResponseEntity =
            wrapRetrofitExceptions(false) {
                loginApi.gosuslugiLogin(loginState, userId)
            }
    }
