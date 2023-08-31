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

package com.mezhendosina.sgo.app.ui.loginFlow.chooseRegion

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.app.ui.loginFlow.chooseRegion.entities.ChooseRegionUiEntity
import com.mezhendosina.sgo.app.ui.loginFlow.chooseRegion.entities.ChooseRegionUiEntityItem
import com.mezhendosina.sgo.app.utils.toDescription
import com.mezhendosina.sgo.app.utils.toLiveData
import com.mezhendosina.sgo.data.SettingsDataStore
import com.mezhendosina.sgo.data.editPreference
import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton
import com.mezhendosina.sgo.data.netschool.repo.RegionsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChooseRegionViewModel(
    private val regionsRepository: RegionsRepository = NetSchoolSingleton.regionsRepository
) : ViewModel() {

    private val _regions = MutableLiveData<ChooseRegionUiEntity>()
    val regions: LiveData<ChooseRegionUiEntity> = _regions

    private val _selectedRegion: MutableLiveData<ChooseRegionUiEntityItem> = MutableLiveData()
    val selectedRegion = _selectedRegion.toLiveData()


    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    init {
        getRegions()
    }

    private fun getRegions() {
        try {
            _errorMessage.value = ""
            _isLoading.value = true
            _regions.value = regionsRepository.getRegions()
            _isLoading.value = false
        } catch (e: Exception) {
            _errorMessage.value = e.toDescription()
        }
    }

    fun editSelectedRegion(newValue: String) {
        _selectedRegion.value = _regions.value?.first { it.name == newValue }!!
    }

    fun setRegion(context: Context, onComplete: () -> Unit) {
        val regionUrl = _selectedRegion.value!!.url
        CoroutineScope(Dispatchers.IO).launch {
            SettingsDataStore.REGION_URL.editPreference(context, regionUrl)
            withContext(Dispatchers.Main) {
                NetSchoolSingleton.baseUrl = regionUrl
                onComplete.invoke()
            }
        }
    }
}