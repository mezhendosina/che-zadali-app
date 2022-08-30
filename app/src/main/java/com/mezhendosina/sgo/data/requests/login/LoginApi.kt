package com.mezhendosina.sgo.data.requests.login

import com.mezhendosina.sgo.data.requests.login.entities.GetDataResponseEntity
import com.mezhendosina.sgo.data.requests.login.entities.LoginRequestEntity
import com.mezhendosina.sgo.data.requests.login.entities.LoginResponseEntity
import com.mezhendosina.sgo.data.requests.login.entities.LogoutRequestEntity
import retrofit2.http.*

interface LoginApi {
    @GET("webapi/logindata") //TODO nssessionid
    suspend fun loginData()

    @POST("webapi/auth/getdata")
    suspend fun getData(): GetDataResponseEntity

    @POST("webapi/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("LoginType") loginType: Int,
        @Field("cid") cid: Int,
        @Field("sid") sid: Int,
        @Field("pid") pid: Int,
        @Field("cn") cn: Int,
        @Field("sft") sft: Int,
        @Field("scid") scid: Int,
        @Field("UN") UN: String,
        @Field("PW") PW: String,
        @Field("lt") lt: String,
        @Field("pw2") pw2: String,
        @Field("ver") ver: String
    ): LoginResponseEntity

    @POST("asp/logout.asp")
    @FormUrlEncoded
    suspend fun logout(@Field("at") at: String)
}