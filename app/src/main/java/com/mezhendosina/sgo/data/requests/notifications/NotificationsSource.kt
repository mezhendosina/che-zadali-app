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

package com.mezhendosina.sgo.data.requests.notifications

import com.google.gson.Gson
import com.mezhendosina.sgo.data.netschool.base.RetrofitConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NotificationsSource {
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