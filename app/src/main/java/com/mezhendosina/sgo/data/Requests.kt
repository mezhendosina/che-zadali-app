package com.mezhendosina.sgo.data

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.mezhendosina.sgo.app.BackendException
import com.mezhendosina.sgo.app.BuildConfig
import com.mezhendosina.sgo.app.ConnectionException
import com.mezhendosina.sgo.app.ParseBackendResponseException
import com.mezhendosina.sgo.data.requests.base.BaseRetrofitSource
import com.mezhendosina.sgo.data.requests.base.RetrofitConfig
import com.mezhendosina.sgo.data.requests.other.entities.checkUpdates.CheckUpdates
import com.mezhendosina.sgo.data.requests.other.entities.schools.SchoolsResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.gson.*
import io.ktor.util.network.*
import kotlinx.coroutines.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.security.MessageDigest
import kotlin.text.toByteArray

// Стырено со stackOverFlow
fun String.toMD5(): String {
    val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
    return bytes.toHex()
}

// Стырено со stackOverFlow
fun ByteArray.toHex(): String {
    return joinToString("") { "%02x".format(it) }
}

// Стырено со stackOverFlow
fun uriFromFile(context: Context, file: File): Uri? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
    } else {
        Uri.fromFile(file)
    }

//class Requests(private val url: String) {
//    private val gson = Gson()
//    private val retrofit = Retrofit.Builder()
//        .baseUrl(url)
//        .addConverterFactory(GsonConverterFactory.create(gson))
//        .client(createOkHttpClient())
//        .build()
//
//    private val config = RetrofitConfig(
//        retrofit = retrofit,
//        gson = gson
//    )
//    val baseRetrofitSource = BaseRetrofitSource(config)
//
//
//    override suspend fun getUpdates(): CheckUpdates =
//        baseRetrofitSource.wrapRetrofitExceptions {
//            otherApi.getLatestUpdate()
//        }
//
//    override suspend fun downloadFile(url: String): ByteArray? =
//        baseRetrofitSource.wrapRetrofitExceptions {
//            otherApi.downloadUpdate(url).body()?.byteStream()?.readBytes()
//
//        }
//
//    override suspend fun getSchools(): SchoolsResponse =
//        baseRetrofitSource.wrapRetrofitExceptions {
//            otherApi.getSchools()
//        }
//
//
//    private fun createOkHttpClient(): OkHttpClient {
//        return OkHttpClient.Builder()
//            .addInterceptor(createLoggingInterceptor())
//            .build()
//    }
//
//    private fun createLoggingInterceptor(): Interceptor {
//        return HttpLoggingInterceptor()
//            .setLevel(HttpLoggingInterceptor.Level.BASIC)
//    }
//}

suspend fun checkUpdates(
    context: Context,
    file: File,
    downloadProgress: MutableLiveData<Int>,
    supportFragmentManager: FragmentManager
) {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson()
        }
    }
    val r: CheckUpdates =
        client.get("https://api.github.com/repos/mezhendosina/che-zadali-app/releases/latest")
            .body()

}

suspend fun schools(): SchoolsResponse =
    try {
        HttpClient(CIO) {
            expectSuccess = true
            install(Logging) {
                level = LogLevel.HEADERS
                logger = Logger.DEFAULT
            }
            install(ContentNegotiation) {
                gson()
            }
        }.get("https://che-zadali-server.herokuapp.com/schools").body()
    } catch (e: Exception) {
        when (e) {
            is ResponseException -> {
                try {
                    throw BackendException(
                        e.response.status.value,
                        e.response.body<Error>().message
                    )
                } catch (exc: Exception) {
                    throw ParseBackendResponseException(exc)
                }
            }
            is UnresolvedAddressException -> {
                throw ConnectionException(e)
            }
            else -> {
                throw ParseBackendResponseException(e)
            }
        }
    }
