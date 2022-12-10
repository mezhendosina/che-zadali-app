package com.mezhendosina.sgo.app.ui.requestRegion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.mezhendosina.sgo.data.requests.sgo.base.BaseRetrofitSource
import com.mezhendosina.sgo.data.requests.sgo.base.RetrofitConfig
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST

interface RegionRequest {
    @POST("/requestRegion")
    suspend fun requestRegion()
}

class RequestRegionViewModel : ViewModel() {

    private val loginInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
    private val client = OkHttpClient.Builder()
        .addInterceptor(loginInterceptor)
        .build()
    private val gson = Gson()

    private val gsonConverterFactory = GsonConverterFactory.create(gson)

    private val retrofit = Retrofit.Builder()
        .baseUrl("")
        .client(client)
        .addConverterFactory(gsonConverterFactory)
        .build()

    private val retrofitConfig = RetrofitConfig(
        retrofit,
        gson
    )

    private val baseRetrofitSource = BaseRetrofitSource(retrofitConfig)
    private val regionRequest = baseRetrofitSource.retrofit.create(RegionRequest::class.java)

    fun sendRequest(regionName: String, regionUrl: String) {
        viewModelScope.launch {
            regionRequest.requestRegion()
        }
    }
}