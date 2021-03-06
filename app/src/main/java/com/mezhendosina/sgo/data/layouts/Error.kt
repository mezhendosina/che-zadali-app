package com.mezhendosina.sgo.data.layouts

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.mezhendosina.sgo.app.databinding.ItemLoadErrorBinding
import com.mezhendosina.sgo.app.ui.errorDialog
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.util.network.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class Error(
    @SerializedName("message")
    val message: String
)

fun responseExceptionHandler(
): CoroutineExceptionHandler = CoroutineExceptionHandler { _, error ->
    CoroutineScope(Dispatchers.Main).launch {
        val message: String = when (error) {
            is ResponseException -> error.response.body<Error>().message
            is UnresolvedAddressException -> "Пропало интернет соединение"
            else -> ""
        }
        throw ResponseMessageException(message)
    }
}

class ResponseMessageException(message: String) : Exception(message)
