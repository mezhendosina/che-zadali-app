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

package com.mezhendosina.sgo.data.netschool.api.homework

import com.mezhendosina.sgo.data.netschool.api.diary.entities.AssignmentTypesResponseEntity
import com.mezhendosina.sgo.data.netschool.api.homework.entities.AssignResponseEntity
import com.mezhendosina.sgo.data.netschool.api.homework.entities.GetAnswerResponseEntity
import retrofit2.http.*

interface HomeworkApi {

    @GET("webapi/grade/assignment/types")
    suspend fun assignmentTypes(
        @Query("all") all: Boolean = true
    ): List<AssignmentTypesResponseEntity>


    @GET("webapi/student/diary/assigns/{assignID}")
    suspend fun getAboutAssign(
        @Path("assignID") assignId: Int,
        @Query("studentId") studentId: Int
    ): AssignResponseEntity


    /**
    Получение прикрепленных ответов на задание
     */
    @GET("webapi/assignments/{assignmentId}/answers")
    suspend fun getAnswer(
        @Path("assignmentId") assignmentId: Int,
        @Query("studentID") studentId: Int
    ): List<GetAnswerResponseEntity>
}
