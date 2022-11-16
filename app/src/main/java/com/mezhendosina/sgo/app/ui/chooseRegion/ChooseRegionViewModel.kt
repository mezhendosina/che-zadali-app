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
            val configSettings = FirebaseRemoteConfigSettings.Builder().apply {
                minimumFetchIntervalInSeconds = 5
            }.build()
            remoteConfig.setConfigSettingsAsync(configSettings)
            remoteConfig.fetchAndActivate().addOnCompleteListener {
                _regions.value = Gson().fromJson(
                    remoteConfig.getValue("regions").asString(),
                    ChooseRegionUiEntity::class.java
                )
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