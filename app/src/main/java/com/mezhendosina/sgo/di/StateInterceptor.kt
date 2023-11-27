package com.mezhendosina.sgo.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class StateInterceptor @Inject constructor(
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newBuilder = chain.request().newBuilder()
//        if (chain.request().url.encodedPath.contains("login") || loggedIn == LoggedIn.TRUE) {
        return chain.proceed(newBuilder.build())

//        }
    }
}