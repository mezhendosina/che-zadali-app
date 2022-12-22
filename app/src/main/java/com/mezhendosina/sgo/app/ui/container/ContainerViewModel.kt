package com.mezhendosina.sgo.app.ui.container

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.BuildConfig
import com.mezhendosina.sgo.app.model.container.ContainerRepository
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.requests.sgo.checkUpdates.CheckUpdates
import com.mezhendosina.sgo.data.uriFromFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class ContainerViewModel(
    private val containerRepository: ContainerRepository = Singleton.containerRepository
) : ViewModel() {

    private val _errorMessage = MutableLiveData("")
    val errorMessage: LiveData<String> = _errorMessage

    private val _latestUpdate = MutableLiveData<CheckUpdates>()
    val latestUpdate: LiveData<CheckUpdates> = _latestUpdate

    private val _downloadState = MutableLiveData(false)
    val downloadState: LiveData<Boolean> = _downloadState

    private val _showUpdateDialog = MutableLiveData(true)
    val showUpdateDialog: LiveData<Boolean> = _showUpdateDialog

    private val settings = Settings(Singleton.getContext())


    init {
        showUpdateDialog()
    }

    fun checkUpdates() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val checkUpdates = containerRepository.checkUpdates()
                withContext(Dispatchers.Main) {
                    _latestUpdate.value = checkUpdates
                }
            } catch (_: Exception) {
            }
        }
    }

    private fun showUpdateDialog() {
        viewModelScope.launch {
            if (BuildConfig.VERSION_CODE > (settings.lastVersionNumber.first() ?: 0)) {
                settings.changeShowUpdateDialog(true)
                settings.changeLastVersionNumber(BuildConfig.VERSION_CODE)
            } else if (settings.showUpdateDialog.first() != false) {
                _showUpdateDialog.value = true
            } else if (settings.showUpdateDialog.first() == false) {
                _showUpdateDialog.value = false
            }
        }
    }

    fun changeUpdateDialogState(b: Boolean) {
        viewModelScope.launch {
            settings.changeShowUpdateDialog(b)
        }
    }

    fun downloadUpdate(context: Context, file: File, url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            //TODO exception handling
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
