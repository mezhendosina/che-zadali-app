package com.mezhendosina.sgo.app.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.LoginActivityBinding
import com.mezhendosina.sgo.app.ui.login.LoginFragment
import com.mezhendosina.sgo.data.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            if (Settings(this@LoginActivity).loggedIn.first()) {
                withContext(Dispatchers.Main) {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
            } else {
                withContext(Dispatchers.Main) {
                    val binding = LoginActivityBinding.inflate(layoutInflater)
                    setContentView(binding.root)

                    setSupportActionBar(binding.toolbar)
                    supportActionBar?.setDisplayShowCustomEnabled(true)
                    supportActionBar?.setTitle(R.string.login_title)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, LoginFragment())
                        .commit()

                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}