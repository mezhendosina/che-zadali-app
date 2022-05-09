package com.mezhendosina.sgo.app.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.MainActivityBinding
import com.mezhendosina.sgo.app.ui.hideAnimation
import com.mezhendosina.sgo.app.ui.journal.JournalFragment
import com.mezhendosina.sgo.app.ui.main.MainFragment
import com.mezhendosina.sgo.app.ui.settings.SettingsFragment
import com.mezhendosina.sgo.app.ui.showAnimation
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.checkUpdates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private val file: File = File.createTempFile("app", "apk")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        showAnimation(binding.progressBar)

        CoroutineScope(Dispatchers.IO).launch {

            val loginData = Settings(this@MainActivity).getLoginData()
            Singleton.login(loginData)
            checkUpdates(this@MainActivity, file)

            withContext(Dispatchers.Main) {
                setContentView(binding.root)
                setSupportActionBar(binding.toolbar)

                supportActionBar?.setDisplayShowCustomEnabled(true)
                supportActionBar?.setTitle(R.string.main)

                if (savedInstanceState == null) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, MainFragment())
                        .commitNow()
                }

                binding.bottomNavigation.setOnItemSelectedListener {
                    when (it.itemId) {
                        R.id.main -> {
                            supportActionBar?.show()
                            supportActionBar?.setTitle(R.string.main)
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, MainFragment())
                                .commitNow()
                            true
                        }
                        R.id.journal -> {
                            supportActionBar?.show()
                            supportActionBar?.setTitle(R.string.journal)
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, JournalFragment())
                                .commitNow()
                            true
                        }
                        R.id.settings -> {
                            supportActionBar?.hide()
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, SettingsFragment()).commitNow()
                            true
                        }
                        else -> false
                    }
                }

                hideAnimation(binding.progressBar)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        file.delete()
        CoroutineScope(Dispatchers.IO).launch {
            Singleton.requests.logout(Singleton.at)
        }
    }
}