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

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.mezhendosina.sgo.app.utils.PermissionNotGranted
import com.mezhendosina.sgo.data.netschool.api.attachments.AttachmentsSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AttachmentsRepository
@Inject constructor(
    private val attachmentsSource: AttachmentsSource
) {


    suspend fun downloadAttachment(
        file: File?,
        attachmentId: Int,
    ): File? {
        val a = if (file != null) CoroutineScope(Dispatchers.IO).async {
            attachmentsSource.downloadAttachment(
                attachmentId,
                file
            )
        } else null

        a?.await()
        return file
    }

    suspend fun deleteAttachment(assignmentId: Int, attachmentId: Int) {
        attachmentsSource.deleteAttachment(assignmentId, attachmentId)
    }

    suspend fun editDescription(attachmentId: Int, description: String): String =
        attachmentsSource.editAttachmentDescription(attachmentId, description)


}