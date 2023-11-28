package com.mezhendosina.sgo.app.model.attachments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.mezhendosina.sgo.app.model.answer.FileUiEntity
import com.mezhendosina.sgo.data.netschool.api.attachments.AttachmentsSource
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject


const val ANNOUNCEMENT = "announcement"
const val HOMEWORK = "homework"

@Module
@InstallIn(SingletonComponent::class)
class AttachmentDownloadManager @Inject constructor(
    private val attachmentsSource: AttachmentsSource
) : AttachmentDownloadManagerInterface {
    override suspend fun downloadFile(
        context: Context,
        assignType: String,
        assignId: Int,
        fileUiEntity: FileUiEntity
    ): String? {
        val file = getFolder(context, assignType, assignId, fileUiEntity.fileName)

        withContext(Dispatchers.IO) {
            file.createNewFile()
        }

        return attachmentsSource.downloadAttachment(fileUiEntity.id!!, file)
    }

    override fun editDescription(attachmentId: Int, description: String?) {
        TODO("Not yet implemented")
    }

    override fun openFile(
        context: Context,
        assignType: String,
        assignId: Int,
        attachmentName: String
    ) {
        val file = getFolder(context, assignType, assignId, attachmentName)
        val intent = Intent(
            Intent.ACTION_VIEW,
            FileProvider.getUriForFile(
                context,
                context.applicationContext.packageName + ".provider",
                file
            ))
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(intent)
    }

    override fun getFolder(
        context: Context,
        assignType: String,
        assignId: Int,
        attachmentName: String
    ): File {
        val downloads = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        downloads?.mkdirs()
        val typesFolder = File(downloads, assignType)
        typesFolder.mkdirs()
        val idFolder = File(typesFolder, assignId.toString())
        idFolder.mkdirs()
        return File(typesFolder, attachmentName)

    }

}