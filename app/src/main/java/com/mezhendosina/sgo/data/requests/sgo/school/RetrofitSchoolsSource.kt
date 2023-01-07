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

package com.mezhendosina.sgo.data.requests.sgo.school

import com.mezhendosina.sgo.app.model.chooseSchool.SchoolsSource
import com.mezhendosina.sgo.data.requests.sgo.base.BaseRetrofitSource
import com.mezhendosina.sgo.data.requests.sgo.base.RetrofitConfig
import com.mezhendosina.sgo.data.requests.sgo.school.entities.SchoolResponseEntity

class RetrofitSchoolsSource(config: RetrofitConfig) : BaseRetrofitSource(config), SchoolsSource {

    private val schoolsApi = retrofit.create(SchoolsApi::class.java)

    override suspend fun getSchools(query: String): List<SchoolResponseEntity> =
        wrapRetrofitExceptions {
            schoolsApi.getSchools(query)
        }
}