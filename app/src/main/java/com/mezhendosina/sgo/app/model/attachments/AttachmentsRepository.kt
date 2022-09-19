package com.mezhendosina.sgo.app.model.attachments

import android.content.Context
import android.content.Intent
import android.webkit.MimeTypeMap
import com.mezhendosina.sgo.app.model.homework.HomeworkSource
import com.mezhendosina.sgo.data.requests.homework.entities.Attachment
import com.mezhendosina.sgo.data.uriFromFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class AttachmentsRepository(
    private val homeworkSource: HomeworkSource
) {
    fun downloadAttachment(
        context: Context,
        attachment: Attachment
    ) {
        val dir = context.cacheDir
        val file = File(dir, attachment.originalFileName)
        val isExist = file.createNewFile()
        CoroutineScope(Dispatchers.IO).launch {
            val contentType = if (!isExist) {
                homeworkSource.downloadAttachment(attachment.id, file)
            } else getMimeType(uriFromFile(context, file).toString())

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uriFromFile(context, file), contentType)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(intent)
        }
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