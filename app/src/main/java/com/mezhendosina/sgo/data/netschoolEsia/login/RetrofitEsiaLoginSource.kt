package com.mezhendosina.sgo.data.netschoolEsia.login

import com.mezhendosina.sgo.data.netschool.api.login.entities.accountInfo.AccountInfoResponseEntity
import com.mezhendosina.sgo.data.netschool.base.BaseRetrofitSource
import com.mezhendosina.sgo.data.netschool.base.RetrofitConfig
import com.mezhendosina.sgo.data.requests.sgo.login.entities.LoginResponseEntity
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitEsiaLoginSource
    @Inject
    constructor(
        config: RetrofitConfig,
    ) : BaseRetrofitSource(config), EsiaLoginSource {
        private val esiaLoginApi = config.baseRetrofit.create(EsiaLoginApi::class.java)

        override suspend fun crossLogin(): Response<ResponseBody> =
            wrapRetrofitExceptions(false) {
                esiaLoginApi.crossLogin()
            }

        override suspend fun getGosuslugiAccountInfo(loginState: String): AccountInfoResponseEntity =
            wrapRetrofitExceptions(false) {
                esiaLoginApi.getAccountInfo(loginState)
            }

        override suspend fun gosuslugiLogin(
            loginState: String,
            userId: String,
        ): LoginResponseEntity =
            wrapRetrofitExceptions(false) {
                esiaLoginApi.gosuslugiLogin(loginState, userId)
            }
    }
