package com.mezhendosina.sgo.data.netschoolEsia.login

import com.mezhendosina.sgo.data.netschool.base.BaseRetrofitSource
import com.mezhendosina.sgo.data.netschool.base.RetrofitConfig
import com.mezhendosina.sgo.data.netschoolEsia.entities.GetTokenResponse
import com.mezhendosina.sgo.data.netschoolEsia.entities.GetUsersResponseItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitLoginSource
    @Inject
    constructor(
        retrofitConfig: RetrofitConfig,
    ) : BaseRetrofitSource(retrofitConfig), LoginSource {
        private val api = retrofitConfig.loginRetrofit.create(IdentityLogin::class.java)

        override suspend fun getToken(deviceCode: Int): GetTokenResponse =
            wrapRetrofitExceptions {
                api.getToken(
                    grantType = "urn:ietf:params:oauth:grant-type:device_code",
                    deviceCode = deviceCode,
                )
            }

        override suspend fun getToken(refreshToken: String): GetTokenResponse =
            wrapRetrofitExceptions {
                api.getToken(
                    grantType = "refresh_token",
                    refreshToken = refreshToken,
                )
            }

        override suspend fun getUsers(): List<GetUsersResponseItem> =
            wrapRetrofitExceptions {
                api.getUsers()
            }
    }
