package com.mezhendosina.sgo.di

import com.google.gson.Gson
import com.mezhendosina.sgo.data.SettingsDataStore
import com.mezhendosina.sgo.di.qualifier.BaseRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BaseRetrofit {
    @Provides
    @Singleton
    fun createMyCookieJar(): MyCookieJar = MyCookieJar()

    @Provides
    @Singleton
    @BaseRetrofit
    fun createRetrofit(
        gson: Gson,
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://localhost/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun createGson(): Gson = Gson()

    @Provides
    @Singleton
    fun createOkHttpClient(
        baseUrlInterceptor: BaseUrlInterceptor,
        headersInterceptor: HeadersInterceptor,
        myCookieJar: MyCookieJar,
        loggingInterceptor: Interceptor,
    ): OkHttpClient {
        val cookieManager = CookieManager()
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        return OkHttpClient.Builder()
            .cookieJar(myCookieJar)
            .addInterceptor(baseUrlInterceptor)
            .addInterceptor(headersInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun createHeadersInterceptor(
        myCookieJar: MyCookieJar,
        settingsDataStore: SettingsDataStore,
    ): HeadersInterceptor = HeadersInterceptor(myCookieJar, settingsDataStore)

    @Provides
    @Singleton
    fun createBaseUrlInterceptor(settingsDataStore: SettingsDataStore): BaseUrlInterceptor = BaseUrlInterceptor(settingsDataStore)

    @Provides
    @Singleton
    fun createLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }
}
