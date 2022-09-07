package com.mezhendosina.sgo.app.activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.data.Settings
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val settings = Settings(this)
        runBlocking {
            AppCompatDelegate.setDefaultNightMode(settings.theme.first())
        }
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch {
            val intent = if (settings.loggedIn.first()) {
                Intent(this@SplashActivity, MainActivity::class.java)
            } else {
                Intent(this@SplashActivity, LoginActivity::class.java)
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }
}