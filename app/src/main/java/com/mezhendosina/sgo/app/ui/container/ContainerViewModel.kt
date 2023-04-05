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
import com.mezhendosina.sgo.data.requests.Download
import com.mezhendosina.sgo.data.requests.github.checkUpdates.CheckUpdates
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

    private val _downloadState = MutableLiveData(0)
    val downloadState: LiveData<Int> = _downloadState


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
            if (BuildConfig.VERSION_CODE > (settings.lastVersionNumber.first())) {
                settings.editPreference(Settings.SHOW_UPDATE_DIALOG, true)
                settings.editPreference(Settings.LAST_VERSION_NUMBER, BuildConfig.VERSION_CODE)
            } else if (settings.showUpdateDialog.first()) {
                _showUpdateDialog.value = true
            } else if (!settings.showUpdateDialog.first()) {
                _showUpdateDialog.value = false
            }
        }
    }

    fun changeUpdateDialogState(b: Boolean) {
        viewModelScope.launch {
            settings.editPreference(Settings.SHOW_UPDATE_DIALOG, b)
        }
    }

    fun downloadUpdate(context: Context, file: File, url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            //TODO exception handling
            containerRepository.downloadFile(url, file).collect {
                when (it) {
                    is Download.Progress -> {
                        withContext(Dispatchers.Main) {
                            _downloadState.value = it.percent
                        }
                    }
                    is Download.Finished -> {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(
                                uriFromFile(context, file),
                                "application/vnd.android.package-archive"
                            )
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        withContext(Dispatchers.Main) { _downloadState.value = 100 }
                        context.startActivity(intent)
                    }
                }
            }
        }
    }
}
