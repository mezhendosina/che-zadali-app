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
