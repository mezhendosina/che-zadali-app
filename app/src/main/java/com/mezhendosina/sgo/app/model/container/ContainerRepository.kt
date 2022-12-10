package com.mezhendosina.sgo.app.model.container

import com.google.gson.Gson
import com.mezhendosina.sgo.data.requests.sgo.base.BaseRetrofitSource
import com.mezhendosina.sgo.data.requests.sgo.base.RetrofitConfig
import com.mezhendosina.sgo.data.requests.sgo.checkUpdates.CheckUpdates
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface UpdateApi {

    @GET("/repos/mezhendosina/che-zadali-app/releases/latest")
    suspend fun getLatestUpdate(): CheckUpdates

    @GET
    suspend fun downloadFile(@Url url: String): Response<ResponseBody>

}

class ContainerRepository {
    private val loginInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
    private val client = OkHttpClient.Builder()
        .addInterceptor(loginInterceptor)
        .build()
    private val gson = Gson()

    private val gsonConverterFactory = GsonConverterFactory.create(gson)

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com")
        .client(client)
        .addConverterFactory(gsonConverterFactory)
        .build()

    private val retrofitConfig = RetrofitConfig(
        retrofit,
        gson
    )

    private val baseRetrofitSource = BaseRetrofitSource(retrofitConfig)

    private val updateApi = baseRetrofitSource.retrofit.create(UpdateApi::class.java)
    suspend fun checkUpdates(): CheckUpdates {
        return baseRetrofitSource.wrapRetrofitExceptions {
            updateApi.getLatestUpdate()
        }
    }

    suspend fun downloadFile(url: String): ByteArray?  {
        return baseRetrofitSource.wrapRetrofitExceptions {
            updateApi.downloadFile(url).body()?.byteStream()?.readBytes()
        }
    }
}