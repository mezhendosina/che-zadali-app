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

package com.mezhendosina.sgo.data

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.nio.charset.Charset
import java.security.MessageDigest

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
