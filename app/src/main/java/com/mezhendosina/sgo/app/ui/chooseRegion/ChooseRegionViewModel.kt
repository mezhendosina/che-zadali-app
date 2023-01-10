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

package com.mezhendosina.sgo.app.ui.chooseRegion

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.activities.MainActivity
import com.mezhendosina.sgo.app.toDescription
import com.mezhendosina.sgo.app.ui.chooseRegion.entities.ChooseRegionUiEntity
import com.mezhendosina.sgo.data.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChooseRegionViewModel : ViewModel() {

    private val _regions = MutableLiveData<ChooseRegionUiEntity>()
    val regions: LiveData<ChooseRegionUiEntity> = _regions

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
            val remoteConfig = FirebaseRemoteConfig.getInstance()
            val configSettings = FirebaseRemoteConfigSettings.Builder().build()
            remoteConfig.setConfigSettingsAsync(configSettings)
            remoteConfig.fetchAndActivate().addOnCompleteListener {
                val result = remoteConfig.getValue("regions").asString()
                _regions.value = if (result.isEmpty()) {
                    Gson().fromJson(
                        "[{\"name\":\"Челябинская область\",\"url\":\"https://sgo.edu-74.ru/\"},{\"name\":\"Алтайский край\",\"url\":\"https://netschool.edu22.info/\"},{\"name\":\"Амурская область\",\"url\":\"https://region.obramur.ru/\"},{\"name\":\"Калужская область\",\"url\":\"https://edu.admoblkaluga.ru:444/\"},{\"name\":\"Костромская область\",\"url\":\"https://netschool.eduportal44.ru/\"},{\"name\":\"Краснодарский край\",\"url\":\"https://sgo.rso23.ru/\"},{\"name\":\"Ленинградская область\",\"url\":\"https://e-school.obr.lenreg.ru/\"},{\"name\":\"Забайкальский край\",\"url\":\"https://region.zabedu.ru/\"}]",
                        ChooseRegionUiEntity::class.java
                    )
                } else {
                    Gson().fromJson(
                        result,
                        ChooseRegionUiEntity::class.java
                    )
                }
                println(_regions.value)
            }
            _isLoading.value = false
        } catch (e: Exception) {
            _errorMessage.value = e.toDescription()
        }
    }

    fun setRegion(fragmentFrom: Int?, regionUrl: String, navController: NavController) {
        CoroutineScope(Dispatchers.IO).launch {
            Settings(Singleton.getContext()).setRegion(regionUrl)
            withContext(Dispatchers.Main) {
                Singleton.baseUrl = regionUrl
                if (fragmentFrom == ChooseRegionFragment.FROM_MAIN_ACTIVITY) {
                    val intent = Intent(Singleton.getContext(), MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(Singleton.getContext(), intent, null)
                } else {
                    navController.navigate(R.id.action_chooseRegionFragment_to_chooseSchoolFragment)
                }
            }
        }
    }
}