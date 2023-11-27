package com.mezhendosina.sgo.di

import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class BaseUrlInterceptor @Inject constructor(
    private val baseUrl: String
) : Interceptor {
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