package com.mezhendosina.sgo.di

import com.google.gson.Gson
import com.mezhendosina.sgo.app.BuildConfig
import com.mezhendosina.sgo.data.SettingsDataStore
import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import okhttp3.Cookie
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.log

data class BaseUrl(val baseUrl: String)

@Module
@InstallIn(SingletonComponent::class)
class BaseRetrofit {

    @Inject
    lateinit var settingsDataStore: SettingsDataStore

    var baseUrl = BaseUrl("")

    @Provides
    @Singleton
    fun createBaseUrl(): BaseUrl = baseUrl

    @Provides
    @Singleton
    fun createMyCookieJar(): MyCookieJar = MyCookieJar()

    @Provides
    @Singleton
    fun createRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
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
        loggingInterceptor: Interceptor
    ): OkHttpClient {
        val cookieManager = CookieManager()
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        return OkHttpClient.Builder()
            .cookieJar(MyCookieJar())
            .addInterceptor(baseUrlInterceptor)
            .addInterceptor(headersInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun createHeadersInterceptor(
        myCookieJar: MyCookieJar,
        baseUrl: BaseUrl,
    ): HeadersInterceptor =
        HeadersInterceptor(myCookieJar, baseUrl.baseUrl)

    @Provides
    @Singleton
    fun createBaseUrlInterceptor(baseUrl: BaseUrl): BaseUrlInterceptor =
        BaseUrlInterceptor(baseUrl.baseUrl)

    @Provides
    @Singleton
    fun createLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }
}