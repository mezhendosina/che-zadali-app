/*
 * Copyright 2023 Eugene Menshenin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mezhendosina.sgo.data.requests.sgo.diary

import com.mezhendosina.sgo.data.requests.sgo.diary.entities.DiaryInitResponseEntity
import com.mezhendosina.sgo.data.requests.sgo.diary.entities.DiaryResponseEntity
import com.mezhendosina.sgo.data.requests.sgo.diary.entities.PastMandatoryEntity
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