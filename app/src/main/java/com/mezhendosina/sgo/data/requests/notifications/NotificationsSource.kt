package com.mezhendosina.sgo.data.requests.notifications

import com.google.gson.Gson
import com.mezhendosina.sgo.data.requests.sgo.base.RetrofitConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NotificationsSource(private val url: String) {
    private val loginInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
    private val client = OkHttpClient.Builder()
        .addInterceptor(loginInterceptor)
        .build()
    private val gson = Gson()

    private val gsonConverterFactory = GsonConverterFactory.create(gson)

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://mezhendosina.site/")
        .client(client)
        .addConverterFactory(gsonConverterFactory)
        .build()

    private val retrofitConfig = RetrofitConfig(
        retrofit,
        gson
    )

    val notificationsSource = RetrofitNotificationsSource(retrofitConfig)

}