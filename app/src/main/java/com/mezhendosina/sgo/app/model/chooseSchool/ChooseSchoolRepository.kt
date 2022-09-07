package com.mezhendosina.sgo.app.model.chooseSchool

import com.google.gson.Gson
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.data.requests.base.BaseRetrofitSource
import com.mezhendosina.sgo.data.requests.base.RetrofitConfig
import com.mezhendosina.sgo.data.requests.other.entities.schools.SchoolItem
import com.mezhendosina.sgo.data.requests.other.entities.schools.SchoolsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET

typealias schoolsActionListener = (List<SchoolItem>) -> Unit

interface ChooseSchoolApi {
    @GET("/schools")
    suspend fun getSchools(): SchoolsResponse
}

class ChooseSchoolRepository {

    var schools = mutableListOf<SchoolItem>()

    private val listeners = mutableSetOf<schoolsActionListener>()

    suspend fun loadSchools() {
        val loginInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
        val client = OkHttpClient.Builder()
            .addInterceptor(loginInterceptor)
            .build()
        val gson = Gson()

        val gsonConverterFactory = GsonConverterFactory.create(gson)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://che-zadali-server.herokuapp.com")
            .client(client)
            .addConverterFactory(gsonConverterFactory)
            .build()

        val retrofitConfig = RetrofitConfig(
            retrofit,
            gson
        )

        val baseRetrofitSource = BaseRetrofitSource(retrofitConfig)

        val schoolApi = baseRetrofitSource.retrofit.create(ChooseSchoolApi::class.java)
        baseRetrofitSource.wrapRetrofitExceptions {
            val schoolsList = schoolApi.getSchools()
            withContext(Dispatchers.Main) {
                schools = schoolsList.schoolItems.toMutableList()
                Singleton.schools = schools
                notifyListeners()
            }
        }


    }

    fun addListener(listener: schoolsActionListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: schoolsActionListener) {
        listeners.remove(listener)
    }

    private fun notifyListeners() {
        listeners.forEach { it.invoke(schools) }
    }


}