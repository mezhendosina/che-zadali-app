package com.mezhendosina.sgo.data.requests

import com.google.gson.Gson
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.Const
import com.mezhendosina.sgo.app.SourcesProvider
import com.mezhendosina.sgo.data.requests.base.RetrofitConfig
import com.mezhendosina.sgo.data.requests.base.RetrofitSourcesProvider
import io.ktor.http.*
import okhttp3.*
import okhttp3.Cookie
import okhttp3.Headers
import okhttp3.internal.cookieToString
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
        cookies.forEach { cookie ->
            val findCookie = cookiesList.find { it.name == cookie.name }
            if (findCookie != null) {
                cookiesList.remove(findCookie)
                cookiesList.add(cookie)
            } else {
                cookiesList.add(cookie)
            }
        }
    }
}

object SourceProviderHolder {

    val sourcesProvider: SourcesProvider by lazy<SourcesProvider> {
        val gson = Gson()
        val config = RetrofitConfig(
            retrofit = createRetrofit(gson),
            gson = gson
        )
        RetrofitSourcesProvider(config)
    }

    private fun createRetrofit(gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Const.BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private fun createOkHttpClient(): OkHttpClient {
        val cookieManager = CookieManager()
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        return OkHttpClient.Builder()
            .cookieJar(MyCookieJar())
            .addInterceptor(createHeadersInterceptor())
            .addInterceptor(createLoggingInterceptor())
            .build()
    }

    private fun createHeadersInterceptor(): Interceptor {
        return Interceptor { chain ->
            val newBuilder = chain.request().newBuilder()

            val headers = Headers.Builder()
                .add("Host", "sgo.edu-74.ru")
                .add("Origin", "https://sgo.edu-74.ru")
                .add("UserAgent", "SGO app")
                .add("X-Requested-With", "XMLHttpRequest")
                .add("Sec-Fetch-Site", "same-origin")
                .add("Sec-Fetch-Mode", "cors")
                .add("Sec-Fetch-Dest", "empty")
                .add("Referer", "https://sgo.edu-74.ru/")
                .add(
                    "sec-ch-ua",
                    "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"101\", \"Microsoft Edge\";v=\"101\""
                )
                .add("Cookie", cookiesList.toCookieString())
                .add("at", Singleton.at)
                .build()
            newBuilder.headers(headers)

            return@Interceptor chain.proceed(newBuilder.build())
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



