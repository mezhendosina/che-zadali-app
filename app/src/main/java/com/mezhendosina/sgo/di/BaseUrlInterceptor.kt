package com.mezhendosina.sgo.di

import com.mezhendosina.sgo.data.SettingsDataStore
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


@Module
@InstallIn(SingletonComponent::class)
class BaseUrlInterceptor @Inject constructor(
    private val settingsDataStore: SettingsDataStore
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
        val url = chain.request().url.toString()
        val a = if (baseUrl.isNotEmpty()) {
            newBuilder.url(url.replace("https://localhost/", baseUrl))
                .build()
        } else {
            newBuilder.url(url).build()
        }
        return chain.proceed(a)
    }
}