package com.mezhendosina.sgo.data.requests.sgo.login

import com.mezhendosina.sgo.data.requests.sgo.login.entities.GetDataResponseEntity
import com.mezhendosina.sgo.data.requests.sgo.login.entities.LoginResponseEntity
import com.mezhendosina.sgo.data.requests.sgo.login.entities.StudentResponseEntity
import retrofit2.Response
import retrofit2.http.*

interface LoginApi {
    @GET("webapi/logindata")
    suspend fun loginData()

    @POST("webapi/auth/getdata")
    suspend fun getData(): GetDataResponseEntity

    @POST("webapi/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("LoginType") loginType: Int,
        @Field("scid") scid: Int,
        @Field("UN") UN: String,
        @Field("PW") PW: String,
        @Field("lt") lt: String,
        @Field("pw2") pw2: String,
        @Field("ver") ver: String
    ): LoginResponseEntity

    @GET("webapi/context/students")
    suspend fun getStudents(): Response<List<StudentResponseEntity>>

    @POST("asp/logout.asp")
    @FormUrlEncoded
    suspend fun logout(@Field("at") at: String)
}