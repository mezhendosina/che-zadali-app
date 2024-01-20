package com.mezhendosina.sgo.app

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.color.DynamicColors
import com.google.firebase.FirebaseApp
import com.mezhendosina.sgo.app.activities.LoginActivity
import com.mezhendosina.sgo.app.activities.MainActivity
import com.mezhendosina.sgo.data.AppSettings
import com.mezhendosina.sgo.data.SettingsDataStore
import com.mezhendosina.sgo.data.netschool.repo.LoginRepositoryInterface
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var settingsDataStore: AppSettings

    @Inject
    lateinit var loginRepositoryInterface: LoginRepositoryInterface

    override fun onCreate() {
        super.onCreate()
        runBlocking {
            AppCompatDelegate.setDefaultNightMode(
                settingsDataStore.getValue(SettingsDataStore.THEME).first()
                    ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
            )
        }
        if (!BuildConfig.DEBUG) DynamicColors.applyToActivitiesIfAvailable(this)
//        DynamicColors.applyToActivitiesIfAvailable(this.application)

        CoroutineScope(Dispatchers.Main).launch {
            FirebaseApp.initializeApp(this@App)

            val intent =
                if (settingsDataStore.getValue(SettingsDataStore.LOGGED_IN).first() == true) {
                    withContext(Dispatchers.IO) {
                        loginRepositoryInterface.login()
                    }
                    Intent(this@App, MainActivity::class.java)
                } else {
                    Intent(this@App, LoginActivity::class.java)
                }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}
