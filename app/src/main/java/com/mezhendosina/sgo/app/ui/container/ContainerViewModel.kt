package com.mezhendosina.sgo.app.ui.container

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.model.announcements.AnnouncementsRepository
import com.mezhendosina.sgo.app.model.container.ContainerRepository
import com.mezhendosina.sgo.data.requests.other.entities.checkUpdates.CheckUpdates
import com.mezhendosina.sgo.data.uriFromFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ContainerViewModel(
    private val containerRepository: ContainerRepository = Singleton.containerRepository
) : ViewModel() {

    private val _errorMessage = MutableLiveData("")
    val errorMessage: LiveData<String> = _errorMessage

    private val _latestUpdate = MutableLiveData<CheckUpdates>()
    val latestUpdate: LiveData<CheckUpdates> = _latestUpdate

    private val _downloadState = MutableLiveData(false)
    val downloadState: LiveData<Boolean> = _downloadState

    fun checkUpdates() {
        viewModelScope.launch {
            _latestUpdate.value = containerRepository.checkUpdates()

        }
    }

    fun downloadUpdate(context: Context, file: File, url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) { _downloadState.value = true }
            val bytes = containerRepository.downloadFile(url)

            if (bytes != null) {
                file.writeBytes(bytes)
            }
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(
                    uriFromFile(context, file),
                    "application/vnd.android.package-archive"
                )
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            withContext(Dispatchers.Main) { _downloadState.value = false }
            context.startActivity(intent)
        }

    }
}
