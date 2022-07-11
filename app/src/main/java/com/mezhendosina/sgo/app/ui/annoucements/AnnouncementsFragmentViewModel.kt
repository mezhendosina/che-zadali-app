package com.mezhendosina.sgo.app.ui.annoucements

import android.content.ActivityNotFoundException
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.ui.errorDialog
import com.mezhendosina.sgo.data.ErrorResponse
import com.mezhendosina.sgo.data.layouts.attachments.Attachment
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AnnouncementsFragmentViewModel: ViewModel() {


    fun downloadAttachment(
        context: Context,
        attachment: Attachment,
        _loading: MutableLiveData<Int>
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Singleton.requests.downloadAttachment(
                    context,
                    Singleton.at,
                    attachment.id,
                    attachment.originalFileName,
                    _loading
                )
            } catch (e: ActivityNotFoundException) {
                withContext(Dispatchers.Main) {
                    _loading.value = 0
                    errorDialog(
                        context,
                        "Похоже,что на устройстве не установлено приложение для открытия этого файла"
                    )
                }
            } catch (e: ResponseException) {
                withContext(Dispatchers.Main) {
                    _loading.value = 0
                    errorDialog(
                        context,
                        e.response.body<ErrorResponse>().message
                    )
                }
            }
        }
    }
}