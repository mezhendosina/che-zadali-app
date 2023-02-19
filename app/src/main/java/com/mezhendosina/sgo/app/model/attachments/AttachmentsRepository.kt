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

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.mezhendosina.sgo.app.model.homework.HomeworkSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.File

class AttachmentsRepository(
    private val homeworkSource: HomeworkSource
) {
    suspend fun downloadAttachment(
        context: Context,
        attachmentId: Int,
        attachmentName: String,
    ) {
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(dir, attachmentName)

        val isExist = withContext(Dispatchers.IO) {
            file.createNewFile()
        }


        val a = if (isExist) CoroutineScope(Dispatchers.IO).async {
            homeworkSource.downloadAttachment(
                attachmentId,
                file
            )
        } else null

        a?.await()
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

    suspend fun deleteAttachment(assignmentId: Int, attachmentId: Int) {
        homeworkSource.deleteAttachment(assignmentId, attachmentId)
    }

    suspend fun editDescription(attachmentId: Int, description: String): String =
        homeworkSource.editAttachmentDescription(attachmentId, description)


    private fun getMimeType(url: String?): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }
}