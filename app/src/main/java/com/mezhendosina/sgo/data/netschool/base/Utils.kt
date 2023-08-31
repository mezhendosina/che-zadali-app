/*
 * Copyright 2023 Eugene Menshenin, Andrey Chechkenev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mezhendosina.sgo.data.netschool.base

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.mezhendosina.sgo.app.utils.getEmojiLesson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import java.io.File
import java.nio.charset.Charset
import java.security.MessageDigest

sealed class Download {
    data class Progress(val percent: Int) : Download()
    data class Finished(val file: File) : Download()
}


// Стырено со StackOverflow,
// модифицировано под алгоритм СГО
fun String.toMD5(): String {
    val bytes = this.toByteArray(Charset.forName("Windows-1251"))
    val hash = MessageDigest.getInstance("MD5").digest(bytes)
    return hash.toHex()
}

// Стырено со StackOverflow
fun ByteArray.toHex(): String {
    return joinToString("") { "%02x".format(it) }
}

// Стырено со StackOverflow,
// отредактировано DarkCat09
// (Unresolved reference: BuildConfig)
fun uriFromFile(context: Context, file: File): Uri? =
    FileProvider.getUriForFile(context, context.packageName + ".provider", file)

fun String.shortcutLesson(lessonsName: List<String>): String {
    if (lessonsName.count { it.contains(this) } > 1) {
        return this
    }
    val lesson = getEmojiLesson(this)
    if (lesson?.name != null) {
        return lesson.name
    }
    return this
}

fun ResponseBody.downloadToFileWithProgress(file: File): Flow<Download> =
    flow {
        emit(Download.Progress(0))

        // flag to delete file if download errors or is cancelled
        var deleteFile = true


        try {
            byteStream().use { inputStream ->
                file.outputStream().use { outputStream ->
                    val totalBytes = contentLength()
                    val data = ByteArray(8_192)
                    var progressBytes = 0L

                    while (true) {
                        val bytes = inputStream.read(data)

                        if (bytes == -1) {
                            break
                        }

                        outputStream.write(data, 0, bytes)
                        progressBytes += bytes

                        emit(Download.Progress(percent = ((progressBytes * 100) / totalBytes).toInt()))
                    }

                    when {
                        progressBytes < totalBytes ->
                            throw Exception("missing bytes")

                        progressBytes > totalBytes ->
                            throw Exception("too many bytes")

                        else ->
                            deleteFile = false
                    }
                }
            }

            emit(Download.Finished(file))
        } finally {
            // check if download was successful

            if (deleteFile) {
                file.delete()
            }
        }
    }
        .flowOn(Dispatchers.IO)
        .distinctUntilChanged()
