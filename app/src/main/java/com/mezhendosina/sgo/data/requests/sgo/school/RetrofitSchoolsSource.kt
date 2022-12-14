package com.mezhendosina.sgo.data.requests.sgo.school

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import com.mezhendosina.sgo.app.model.chooseSchool.SchoolsSource
import com.mezhendosina.sgo.data.requests.sgo.base.BaseRetrofitSource
import com.mezhendosina.sgo.data.requests.sgo.base.RetrofitConfig
import com.mezhendosina.sgo.data.requests.sgo.school.entities.SchoolResponseEntity
import retrofit2.HttpException

class RetrofitSchoolsSource(config: RetrofitConfig) : BaseRetrofitSource(config), SchoolsSource {

    private val schoolsApi = retrofit.create(SchoolsApi::class.java)

    override suspend fun getSchools(): List<SchoolResponseEntity> = wrapRetrofitExceptions {
        val gson = Gson()
        try {
            val a = schoolsApi.getSchools()
            if (a.isSuccessful) {
                gson.fromJson(a.body()?.string(), Array<SchoolResponseEntity>::class.java).toList()
            } else {
                val firebaseConfig =
                    FirebaseRemoteConfig.getInstance().getValue("schools").asString()
                gson.fromJson(
                    firebaseConfig,
                    Array<SchoolResponseEntity>::class.java
                ).toList()
            }

        } catch (e: HttpException) {
            val firebaseConfig =
                FirebaseRemoteConfig.getInstance().getValue("schools").asString()
            gson.fromJson(
                firebaseConfig,
                Array<SchoolResponseEntity>::class.java
            ).toList()

        }

    }
}