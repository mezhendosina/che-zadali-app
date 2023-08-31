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

package com.mezhendosina.sgo.app.ui.main.container

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.BuildConfig
import com.mezhendosina.sgo.app.model.ContainerRepository
import com.mezhendosina.sgo.app.utils.toLiveData
import com.mezhendosina.sgo.data.SettingsDataStore
import com.mezhendosina.sgo.data.WeekStartEndEntity
import com.mezhendosina.sgo.data.editPreference
import com.mezhendosina.sgo.data.getValue
import com.mezhendosina.sgo.data.getWeeksList
import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton
import com.mezhendosina.sgo.data.netschool.base.Download
import com.mezhendosina.sgo.data.netschool.base.uriFromFile
import com.mezhendosina.sgo.data.requests.github.checkUpdates.CheckUpdates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class ContainerViewModel(
//    private val githubUpdateDownloader: GithubUpdateDownloader = NetSchoolSingleton.githubUpdateDownloader
    private val containerRepository: ContainerRepository = NetSchoolSingleton.containerRepository
) : ViewModel() {

    private val _errorMessage = MutableLiveData("")
    val errorMessage: LiveData<String> = _errorMessage

    private val _latestUpdate = MutableLiveData<CheckUpdates>()
    val latestUpdate: LiveData<CheckUpdates> = _latestUpdate

    private val _downloadState = MutableLiveData(0)
    val downloadState: LiveData<Int> = _downloadState


    private val _showUpdateDialog = MutableLiveData(true)
    val showUpdateDialog: LiveData<Boolean> = _showUpdateDialog

    private val _showEngageDialog = MutableLiveData(false)
    val showEngageDialog = _showEngageDialog.toLiveData()

    private val _weeks = MutableLiveData<List<WeekStartEndEntity>>()
    val weeks: LiveData<List<WeekStartEndEntity>> = _weeks

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

    fun showUpdateDialog(context: Context) {
        viewModelScope.launch {
            val lastVersionNumber =
                SettingsDataStore.LAST_VERSION_NUMBER.getValue(context, -1).first()
            val showUpdateDialog =
                SettingsDataStore.SHOW_UPDATE_DIALOG.getValue(context, true).first()
            if (BuildConfig.VERSION_CODE > lastVersionNumber) {
                SettingsDataStore.SHOW_UPDATE_DIALOG.editPreference(context, true)
                SettingsDataStore.LAST_VERSION_NUMBER.editPreference(
                    context,
                    BuildConfig.VERSION_CODE
                )
                if (BuildConfig.VERSION_CODE == 35) {
                    _showEngageDialog.value = true
                }
            } else if (showUpdateDialog) {
                _showUpdateDialog.value = true
            } else if (!showUpdateDialog) {
                _showUpdateDialog.value = false
            }
        }
    }

    fun changeUpdateDialogState(context: Context, b: Boolean) {
        viewModelScope.launch {
            SettingsDataStore.SHOW_UPDATE_DIALOG.editPreference(context, b)
        }
    }

    fun downloadUpdate(context: Context, file: File, url: String) {
//        //TODO exception handling
//        githubUpdateDownloader.downloadUpdate(context, url) { progress, uri ->
//            when (progress) {
//                100 -> {
//                    val intent = Intent(Intent.ACTION_VIEW).apply {
//                        setDataAndType(
//                            uri,
//                            "application/vnd.android.package-archive"
//                        )
//                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                    }
//                    _downloadState.value = 100
//                    context.startActivity(intent)
//                }
//
//                else -> {
//                    _downloadState.value = progress
//                }
//            }
//        }
        CoroutineScope(Dispatchers.IO).launch {
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

    suspend fun loadWeeks() {
        if (Singleton.weeks.isNotEmpty()) {
            withContext(Dispatchers.Main) {
                _weeks.value = Singleton.weeks
            }
        } else {
            val a = getWeeksList()
            withContext(Dispatchers.Main) {
                _weeks.value = a
            }

        }
    }
}
