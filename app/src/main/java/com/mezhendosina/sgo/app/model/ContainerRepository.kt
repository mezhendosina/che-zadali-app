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

package com.mezhendosina.sgo.app.model

import com.google.gson.Gson
import com.mezhendosina.sgo.data.netschool.base.RetrofitConfig
import com.mezhendosina.sgo.data.netschool.base.BaseRetrofitSource
import com.mezhendosina.sgo.data.netschool.base.Download
import com.mezhendosina.sgo.data.netschool.base.downloadToFileWithProgress
import com.mezhendosina.sgo.data.requests.github.checkUpdates.CheckUpdates
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url
import java.io.File
import javax.inject.Inject

interface UpdateApi {
    @GET("/repos/mezhendosina/che-zadali-app/releases/latest")
    suspend fun getLatestUpdate(): CheckUpdates

    @GET
    @Streaming
    suspend fun downloadFile(
        @Url url: String,
    ): ResponseBody
}

@Module
@InstallIn(SingletonComponent::class)
class ContainerRepository
    @Inject
    constructor() {
        private val loginInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
        private val client =
            OkHttpClient.Builder()
                .addInterceptor(loginInterceptor)
                .build()
        private val gson = Gson()

        private val gsonConverterFactory = GsonConverterFactory.create(gson)

        private val retrofit =
            Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .client(client)
                .addConverterFactory(gsonConverterFactory)
                .build()

        private val retrofitConfig =
            RetrofitConfig(
                retrofit,,,
                gson
            )

        private val baseRetrofitSource = BaseRetrofitSource(retrofitConfig)

        private val updateApi = baseRetrofitSource.retrofit.create(UpdateApi::class.java)

        suspend fun checkUpdates(): CheckUpdates {
            return baseRetrofitSource.wrapRetrofitExceptions {
                updateApi.getLatestUpdate()
            }
        }

        suspend fun downloadFile(
            url: String,
            file: File,
        ): Flow<Download> =
            baseRetrofitSource.wrapRetrofitExceptions {
                updateApi.downloadFile(url).downloadToFileWithProgress(file)
            }
    }
