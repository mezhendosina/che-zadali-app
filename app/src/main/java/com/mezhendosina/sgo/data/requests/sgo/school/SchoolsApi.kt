package com.mezhendosina.sgo.data.requests.sgo.school

import com.mezhendosina.sgo.data.requests.sgo.school.entities.SchoolResponseEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface SchoolsApi {

    @GET("webapi/schools/search")
    suspend fun getSchools(
        @Query("name") schoolName: String
    ): List<SchoolResponseEntity>
}