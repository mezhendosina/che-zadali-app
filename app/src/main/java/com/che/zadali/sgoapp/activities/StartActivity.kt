package com.che.zadali.sgoapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.che.zadali.sgoapp.data.services.SettingsPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineScope(Dispatchers.Main).launch{
            val a = SettingsPrefs(this@StartActivity).theme.first()
            AppCompatDelegate.setDefaultNightMode(a)
        }

    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.IO).launch {
            if (SettingsPrefs(this@StartActivity).loggedIn.firstOrNull() == true) {
                startActivity(Intent(this@StartActivity, MainActivity::class.java))
            } else {
                startActivity(Intent(this@StartActivity, LoginActivity::class.java))
            }
            finish()
        }
    }
}