package com.mezhendosina.sgo.di

import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.BuildConfig
import com.mezhendosina.sgo.data.SettingsDataStore
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


@Module
@InstallIn(SingletonComponent::class)
class HeadersInterceptor
@Inject constructor(
    private val myCookieJar: MyCookieJar,
    private val settingsDataStore: SettingsDataStore,
) : Interceptor {


    private var baseUrl = ""

    init {
        CoroutineScope(Dispatchers.IO).launch {
            settingsDataStore.getValue(SettingsDataStore.REGION_URL).collect {
                if (it != null){
                    baseUrl = it
                }
            }
        }
    }
    override fun intercept(chain: Interceptor.Chain): Response {
        val newBuilder = chain.request().newBuilder()
        if (baseUrl.isNotEmpty()) {
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
                .add("Cookie", myCookieJar.toCookieString())
                .add("at", Singleton.at)
                .build()
            newBuilder.headers(headers)
        }
        return chain.proceed(newBuilder.build())


    }
}