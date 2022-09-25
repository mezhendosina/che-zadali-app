package com.mezhendosina.sgo.app.model.attachments

import android.content.Context
import android.content.Intent
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.mezhendosina.sgo.app.BuildConfig
import com.mezhendosina.sgo.app.model.homework.HomeworkSource
import com.mezhendosina.sgo.data.requests.homework.entities.Attachment
import kotlinx.coroutines.*
import java.io.File

class AttachmentsRepository(
    private val homeworkSource: HomeworkSource
) {
    suspend fun downloadAttachment(
        context: Context,
        attachment: Attachment
    ) {
        val dir = context.filesDir
        val file = File(dir, attachment.originalFileName)

        val isExist = withContext(Dispatchers.IO) {
            file.createNewFile()
        }


        val a = if (isExist) CoroutineScope(Dispatchers.IO).async {
            homeworkSource.downloadAttachment(
                attachment.id,
                file
            )
        } else null

        a?.await()
        val contentType = getMimeType(file.toURI().toString())
        val fileUri =
            FileProvider.getUriForFile(
                context,
                BuildConfig.APPLICATION_ID + ".provider",
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