package com.che.zadali.sgoapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.che.zadali.sgoapp.data.SettingsPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
            if (SettingsPrefs(this@StartActivity).loggedIn.firstOrNull() == true) {
                startActivity(Intent(this@StartActivity, MainActivity::class.java))
            }else{
                startActivity(Intent(this@StartActivity, Activity::class.java))
            }
            finish()
        }
    }
}