package com.mezhendosina.sgo.app.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.color.DynamicColors
//import com.google.firebase.FirebaseApp
import com.mezhendosina.sgo.app.BuildConfig
import com.mezhendosina.sgo.data.AppSettings
import com.mezhendosina.sgo.data.SettingsDataStore
import com.mezhendosina.sgo.data.netschool.repo.LoginRepositoryInterface
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class RoutingActivity : AppCompatActivity() {
    @Inject
    lateinit var settingsDataStore: AppSettings

    @Inject
    lateinit var loginRepositoryInterface: LoginRepositoryInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        runBlocking {
            AppCompatDelegate.setDefaultNightMode(
                settingsDataStore.getValue(SettingsDataStore.THEME).first()
                    ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
            )
        }
        if (!BuildConfig.DEBUG) DynamicColors.applyToActivitiesIfAvailable(application)
//        DynamicColors.applyToActivitiesIfAvailable(this.application)

        CoroutineScope(Dispatchers.Main).launch {
           // FirebaseApp.initializeApp(this@RoutingActivity)

            val intent =
                if (settingsDataStore.getValue(SettingsDataStore.LOGGED_IN).first() == true) {
                    withContext(Dispatchers.IO) {
                        loginRepositoryInterface.login()
                    }
                    Intent(this@RoutingActivity, MainActivity::class.java)
                } else {
                    Intent(this@RoutingActivity, LoginActivity::class.java)
                }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}
