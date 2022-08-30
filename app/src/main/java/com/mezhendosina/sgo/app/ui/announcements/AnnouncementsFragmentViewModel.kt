package com.mezhendosina.sgo.app.ui.announcements

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.webkit.MimeTypeMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.model.homework.HomeworkSource
import com.mezhendosina.sgo.app.toDescription
import com.mezhendosina.sgo.data.requests.homework.entities.Attachment
import com.mezhendosina.sgo.data.uriFromFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


class AnnouncementsFragmentViewModel(
    private val homeworkSource: HomeworkSource = Singleton.homeworkSource
) : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun downloadAttachment(
        context: Context,
        attachment: Attachment,
    ) {
        val dir = context.dataDir
        val file = File(dir, attachment.originalFileName)
        val isExist = file.createNewFile()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val contentType = if (!isExist) {
                    homeworkSource.downloadAttachment(attachment.id, file)
                } else getMimeType(uriFromFile(context, file).toString())

                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uriFromFile(context, file), contentType)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(intent)
            } catch (e: Exception) {
                _errorMessage.value =
                    if (e is ActivityNotFoundException) "Похоже, что на устройстве не установлено приложение для открытия этого файла"
                    else e.toDescription()
            }
        }
    }
}

fun getMimeType(url: String?): String? {
    var type: String? = null
    val extension = MimeTypeMap.getFileExtensionFromUrl(url)
    if (extension != null) {
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }
    return type
}