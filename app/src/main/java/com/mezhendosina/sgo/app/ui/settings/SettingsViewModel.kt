package com.mezhendosina.sgo.app.ui.settings

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.with
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.activities.LoginActivity
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.layouts.mySettings.MySettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class SettingsViewModel : ViewModel() {

    private val _mySettings = MutableLiveData<MySettings>()
    val mySettings: LiveData<MySettings> = _mySettings

    private val file = File.createTempFile("profilePhoto", "index")

    init {
        getMySettings()
    }

    private fun getMySettings() {
        CoroutineScope(Dispatchers.IO).launch() {
            val a = Singleton.requests.mySettings(Singleton.at)
            withContext(Dispatchers.Main) {
                _mySettings.value = a
            }
        }
    }

    fun loadProfilePhoto(context: Context, photoView: ImageView) {
        CoroutineScope(Dispatchers.IO).launch {
            val settings = Settings(context)
            Singleton.requests.loadPhoto(Singleton.at, settings.currentUserId.first(), file)
            withContext(Dispatchers.Main) {
                Glide.with(context).load(file).circleCrop().into(photoView)
            }
        }
    }

    fun changeTheme(selectedThemeId: Int) {
        when (selectedThemeId) {
            1, 2 -> AppCompatDelegate.setDefaultNightMode(selectedThemeId)
            3 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }

    }

    fun logout(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            Settings(context).logout()
        }
    }

    fun calculateCache(context: Context): Long = context.cacheDir.calculateSizeRecursively()

    private fun File.calculateSizeRecursively(): Long {
        return walkBottomUp().fold(0L) { acc, file -> acc + file.length() }
    }

    override fun onCleared() {
        super.onCleared()
        file.delete()
    }
}