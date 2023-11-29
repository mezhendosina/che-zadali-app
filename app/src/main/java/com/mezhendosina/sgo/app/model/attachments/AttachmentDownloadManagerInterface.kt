package com.mezhendosina.sgo.app.model.attachments

import android.content.Context
import com.mezhendosina.sgo.app.model.answer.FileUiEntity
import java.io.File

interface AttachmentDownloadManagerInterface {

    suspend fun downloadFile(
        context: Context,
        fileUiEntity: FileUiEntity
    ): String?

    suspend fun uploadFiles(
        context: Context,
        files: List<FileUiEntity>
    )


    fun editDescription(attachmentId: Int, description: String?)

    fun openFile(context: Context, fileUiEntity: FileUiEntity)

    fun getFile(context: Context, assignType: String, assignId: Int, attachmentName: String): File
}
