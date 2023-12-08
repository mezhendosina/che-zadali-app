/*
 * Copyright 2023 Eugene Menshenin
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

package com.mezhendosina.sgo.app.model.attachments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.mezhendosina.sgo.app.utils.PermissionNotGranted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class AttachmentsUtils {

    companion object {


        fun getFile(context: Context, name: String): File {
            if (!checkPermissions(context)) throw PermissionNotGranted()
            val downloadsFolder = getDownloadsFolder(context)
            return File(downloadsFolder, name)
        }

        suspend fun createFile(context: Context, name: String): File? {
            val file = getFile(context, name)
            val isExist = withContext(Dispatchers.IO) {
                file.createNewFile()
            }
            return if (isExist) file else null
        }

        fun checkPermissions(context: Context) =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED


        fun getDownloadsFolder(context: Context): File {
            val downloadsFolder = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            val appDownloadFolder = File(downloadsFolder, "SGO app")
            appDownloadFolder.mkdir()
            return appDownloadFolder
        }

        fun openFile(context: Context, file: File) {
            val contentType = getMimeType(file.toURI().toString())

            val fileUri = FileProvider.getUriForFile(
                context,
                context.applicationContext.packageName + ".provider",
                file
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(fileUri, contentType)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(intent)
        }

        private fun getMimeType(url: String?): String? {
            var type: String? = null
            val extension = MimeTypeMap.getFileExtensionFromUrl(url)
            if (extension != null) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            }
            return type
        }

    }


}