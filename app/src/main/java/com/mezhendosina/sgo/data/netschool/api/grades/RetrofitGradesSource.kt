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

package com.mezhendosina.sgo.data.netschool.api.grades

import com.mezhendosina.sgo.app.model.grades.GradesSource
import com.mezhendosina.sgo.data.netschool.base.BaseRetrofitSource
import com.mezhendosina.sgo.data.netschool.base.RetrofitConfig
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitGradesSource @Inject constructor(config: RetrofitConfig) :
    BaseRetrofitSource(config), GradesSource {

    private val gradesApi = retrofit.create(GradesApi::class.java)

    override suspend fun getParentInfoLetter(at: String): Response<ResponseBody> =
        wrapRetrofitExceptions {
            gradesApi.getParentInfoLetter(at)
        }

    override suspend fun getGrades(
        at: String,
        pclid: String,
        reportType: String,
        termID: String,
        sid: String
    ): Response<ResponseBody> = wrapRetrofitExceptions {
        gradesApi.getGrades(
            at = at,
            pclid = pclid,
            reportType = reportType,
            termId = termID,
            sid = sid
        )
    }

}