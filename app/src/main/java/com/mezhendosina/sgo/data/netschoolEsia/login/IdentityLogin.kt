package com.mezhendosina.sgo.data.netschoolEsia.login

import com.mezhendosina.sgo.data.netschoolEsia.entities.login.GetTokenResponse
import com.mezhendosina.sgo.data.netschoolEsia.entities.login.GetUsersResponseItem
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface IdentityLogin {
    @POST("connect/token")
    @FormUrlEncoded
    fun getToken(
        @Field("grant_type") grantType: String,
        @Field("device_code") deviceCode: Int? = null,
        @Field("refresh_token") refreshToken: String? = null,
        @Field("client_id") clientId: String = "parent-mobile",
        @Field("client_secret") clientSecret: String = "04064338-13df-4747-8dea-69849f9ecdf0",
    ): GetTokenResponse

    @POST("users")
    @FormUrlEncoded
    fun getUsers(): List<GetUsersResponseItem>
}
