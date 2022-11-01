package com.mezhendosina.sgo.data

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.mezhendosina.sgo.app.BuildConfig
import java.io.File
import java.security.MessageDigest

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
    FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
