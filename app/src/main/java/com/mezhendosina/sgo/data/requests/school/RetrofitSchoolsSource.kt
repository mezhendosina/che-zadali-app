package com.mezhendosina.sgo.data.requests.school

import com.mezhendosina.sgo.app.model.chooseSchool.SchoolsSource
import com.mezhendosina.sgo.data.requests.base.BaseRetrofitSource
import com.mezhendosina.sgo.data.requests.base.RetrofitConfig
import com.mezhendosina.sgo.data.requests.school.entities.SchoolResponseEntity

class RetrofitSchoolsSource(config: RetrofitConfig) : BaseRetrofitSource(config), SchoolsSource {

    private val schoolsApi = retrofit.create(SchoolsApi::class.java)

    override suspend fun getSchools(): List<SchoolResponseEntity> = wrapRetrofitExceptions {
        schoolsApi.getSchools()
    }
}