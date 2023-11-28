package com.mezhendosina.sgo.app.model.attachments

import android.accounts.AuthenticatorDescription
import android.app.DownloadManager.Request
import android.content.Context
import com.mezhendosina.sgo.app.model.answer.FileUiEntity
import java.io.File

interface AttachmentDownloadManagerInterface {

    suspend fun downloadFile(
        context: Context,
        assignType: String,
        assignId: Int,
        fileUiEntity: FileUiEntity
    ): String?

    fun editDescription(attachmentId: Int, description: String?)

    fun openFile(context: Context, assignType: String, assignId: Int, attachmentName: String)

    fun getFolder(context: Context, assignType: String, assignId: Int, attachmentName: String): File
}
