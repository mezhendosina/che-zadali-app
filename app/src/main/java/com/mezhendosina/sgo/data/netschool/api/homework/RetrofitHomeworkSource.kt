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
import com.mezhendosina.sgo.data.netschool.base.BaseRetrofitSource
import com.mezhendosina.sgo.data.netschool.base.RetrofitConfig
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RetrofitHomeworkSource @Inject constructor(config: RetrofitConfig) :
    BaseRetrofitSource(config), HomeworkSource {

    private val homeworkSource = retrofit.create(HomeworkApi::class.java)


    override suspend fun assignmentTypes(all: Boolean): List<AssignmentTypesResponseEntity> =
        wrapRetrofitExceptions {
            homeworkSource.assignmentTypes(all)
        }


    override suspend fun getAnswer(
        assignmentId: Int,
        studentId: Int
    ): List<GetAnswerResponseEntity> =
        wrapRetrofitExceptions {
            homeworkSource.getAnswer(assignmentId, studentId)
        }

    override suspend fun getAboutAssign(assignId: Int, studentId: Int): AssignResponseEntity =
        wrapRetrofitExceptions {
            homeworkSource.getAboutAssign(assignId, studentId)
        }


}