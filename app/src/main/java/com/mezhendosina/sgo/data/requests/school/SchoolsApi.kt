package com.mezhendosina.sgo.data.requests.school

import com.mezhendosina.sgo.data.requests.school.entities.SchoolResponseEntity
import retrofit2.http.GET

interface SchoolsApi {

    @GET("webapi/addresses/schools")
    suspend fun getSchools(): List<SchoolResponseEntity>
}