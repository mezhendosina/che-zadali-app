package com.mezhendosina.sgo.data.requests.sgo.school

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface SchoolsApi {

    @GET("webapi/addresses/schools")
    suspend fun getSchools(): Response<ResponseBody>
}