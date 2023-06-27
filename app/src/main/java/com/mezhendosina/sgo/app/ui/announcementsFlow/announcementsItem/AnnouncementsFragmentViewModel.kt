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

package com.mezhendosina.sgo.app.ui.announcementsFlow.announcementsItem

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.app.databinding.ItemAttachmentBinding
import com.mezhendosina.sgo.app.model.attachments.AttachmentsRepository
import com.mezhendosina.sgo.app.utils.PermissionNotGranted
import com.mezhendosina.sgo.app.utils.toDescription
import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton
import com.mezhendosina.sgo.data.netschool.api.attachments.entities.Attachment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class AnnouncementsFragmentViewModel(
    private val attachmentsRepository: AttachmentsRepository = NetSchoolSingleton.attachmentsRepository
) : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    suspend fun downloadAttachment(
        context: Context,
        attachment: Attachment,
        binding: ItemAttachmentBinding
    ) {
        try {
            attachmentsRepository.downloadAttachment(
                context,
                attachment.id,
                attachment.originalFileName
            )
        } catch (e: Exception) {
            if (e is PermissionNotGranted) {
                throw PermissionNotGranted()
            } else {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = e.toDescription()
                }
            }
        } finally {
            withContext(Dispatchers.Main) {
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}

