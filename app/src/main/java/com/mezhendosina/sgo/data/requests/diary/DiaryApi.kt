package com.mezhendosina.sgo.data.requests.diary

import com.mezhendosina.sgo.data.requests.diary.entities.*
import retrofit2.http.*

interface DiaryApi {

    @GET("webapi/student/diary/init")
    suspend fun diaryInit(): DiaryInitResponseEntity

    @GET("webapi/student/diary")
    suspend fun diary(
        @Query("studentId") studentId: Int,
        @Query("weekEnd") weekEnd: String,
        @Query("weekStart") weekStart: String,
        @Query("withLaAssigns") withLaAssigns: Boolean,
        @Query("yearId") yearId: Int
    ): DiaryResponseEntity


    @GET("webapi/student/diary/pastMandatory")
    suspend fun getPastMandatory(
        @Query("studentID") studentInt: Int,
        @Query("weekEnd") weekEnd: String,
        @Query("weekStart") weekStart: String,
        @Query("yearId") yearId: Int
    ): List<PastMandatoryEntity>


}