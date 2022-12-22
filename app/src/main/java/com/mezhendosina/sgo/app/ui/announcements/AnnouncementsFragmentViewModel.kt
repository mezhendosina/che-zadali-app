package com.mezhendosina.sgo.app.ui.announcements

import android.content.ActivityNotFoundException
import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.databinding.ItemAttachmentBinding
import com.mezhendosina.sgo.app.model.attachments.AttachmentsRepository
import com.mezhendosina.sgo.app.toDescription
import com.mezhendosina.sgo.data.requests.sgo.homework.entities.Attachment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class AnnouncementsFragmentViewModel(
    private val attachmentsRepository: AttachmentsRepository = Singleton.attachmentsRepository
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
            withContext(Dispatchers.Main) {
                _errorMessage.value =
                    if (e is ActivityNotFoundException) "Похоже, что на устройстве не установлено приложение для открытия этого файла"
                    else e.toDescription()
            }
        } finally {
            withContext(Dispatchers.Main) {
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}

