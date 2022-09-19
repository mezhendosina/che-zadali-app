package com.mezhendosina.sgo.app.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.data.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val settings = Settings(this)
        runBlocking {
            AppCompatDelegate.setDefaultNightMode(settings.theme.first())
        }
        super.onCreate(savedInstanceState)

        Singleton.loadContext(applicationContext)

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