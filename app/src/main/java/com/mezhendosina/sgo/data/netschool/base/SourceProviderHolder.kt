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

package com.mezhendosina.sgo.data.netschool.base

import com.google.gson.Gson
import com.mezhendosina.sgo.app.BuildConfig
import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton
import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton.baseUrl
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy

private val cookiesList = mutableListOf<Cookie>()

class MyCookieJar : CookieJar {

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookiesList
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val cookieCopy = cookies.toMutableList()

        cookiesList.replaceAll { oldCookie ->
            val findCookie = cookieCopy.find { it.name == oldCookie.name }
            cookieCopy.remove(findCookie)
            findCookie ?: oldCookie
        }
        cookiesList.addAll(cookieCopy)

    }
}

object SourceProviderHolder {

    val sourcesProvider: SourcesProvider by lazy<SourcesProvider> {
        val gson = Gson().newBuilder().setLenient().create()


        val config = RetrofitConfig(
            retrofit = createRetrofit(gson),
            gson = gson
        )

        RetrofitSourcesProvider(config)
    }


    private fun createRetrofit(gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://localhost/")
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private fun createOkHttpClient(): OkHttpClient {
        val cookieManager = CookieManager()
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        return OkHttpClient.Builder()
            .cookieJar(MyCookieJar())
            .addInterceptor(createBaseUrlInterceptor())
            .addInterceptor(createHeadersInterceptor())
            .addInterceptor(createLoggingInterceptor())
            .build()
    }

    private fun createHeadersInterceptor(): Interceptor {
        return Interceptor { chain ->
            val newBuilder = chain.request().newBuilder()
            val headers = Headers.Builder()
                .add("Host", baseUrl.replace("https://", "").dropLast(1))
                .add("Origin", baseUrl)
                .add("UserAgent", "SGO app v${BuildConfig.VERSION_NAME}")
                .add("X-Requested-With", "XMLHttpRequest")
                .add("Sec-Fetch-Site", "same-origin")
                .add("Sec-Fetch-Mode", "cors")
                .add("Sec-Fetch-Dest", "empty")
                .add("Referer", baseUrl)
                .add(
                    "sec-ch-ua",
                    "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"105\", \"Microsoft Edge\";v=\"105\""
                )
                .add("Cookie", cookiesList.toCookieString())
                .add("at", NetSchoolSingleton.at)
                .build()
            newBuilder.headers(headers)

            return@Interceptor chain.proceed(newBuilder.build())
        }
    }

    private fun createBaseUrlInterceptor(): Interceptor {
        return Interceptor { chain ->

            val newBuilder = chain.request().newBuilder()
            val url = chain.request().url.toString()
            val a = if (baseUrl.isNotEmpty()) {
                newBuilder.url(url.replace("https://localhost/", baseUrl)).build()
            } else {
                newBuilder.url(url).build()
            }
            return@Interceptor chain.proceed(a)
        }
    }

    private fun createLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private fun List<Cookie?>.toCookieString(): String {
        var cookiesString = ""

        this.forEach {
            if (it != null) {
                cookiesString += "${it.name}=${it.value}; "
            }
        }
        return cookiesString
    }
}



