package com.che.zadali.sgo_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import com.che.zadali.sgo_app.data.SettingsPrefs
import kotlinx.coroutines.flow.map

@ExperimentalAnimationApi
class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SettingsPrefs(this).loggedIn.map {
            if (it) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }
}