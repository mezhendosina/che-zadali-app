package com.mezhendosina.sgo.app.model.chooseSchool

import com.mezhendosina.sgo.data.requests.sgo.school.entities.SchoolResponseEntity

interface SchoolsSource {

    suspend fun getSchools(): List<SchoolResponseEntity>
}