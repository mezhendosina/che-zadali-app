package com.mezhendosina.sgo.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

@Module
@InstallIn(SingletonComponent::class)
class MyCookieJar : CookieJar {

    private val cookiesList = mutableListOf<Cookie>()


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

    fun toCookieString(): String {
        var cookiesString = ""

        cookiesList.forEach {
            if (it != null) {
                cookiesString += "${it.name}=${it.value}; "
            }
        }
        return cookiesString
    }
}