package com.mezhendosina.sgo.data.requests.sgo.school

import com.mezhendosina.sgo.app.model.chooseSchool.SchoolsSource
import com.mezhendosina.sgo.data.requests.sgo.base.BaseRetrofitSource
import com.mezhendosina.sgo.data.requests.sgo.base.RetrofitConfig
import com.mezhendosina.sgo.data.requests.sgo.school.entities.SchoolResponseEntity

class RetrofitSchoolsSource(config: RetrofitConfig) : BaseRetrofitSource(config), SchoolsSource {

    private val schoolsApi = retrofit.create(SchoolsApi::class.java)

    override suspend fun getSchools(): List<SchoolResponseEntity> = wrapRetrofitExceptions {
        schoolsApi.getSchools()
    }
}