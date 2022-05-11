package com.mezhendosina.sgo.app.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.Navigator
import com.mezhendosina.sgo.app.databinding.ContainerBinding
import com.mezhendosina.sgo.app.ui.errorDialog
import com.mezhendosina.sgo.app.ui.main.MainScreenFragment
import com.mezhendosina.sgo.app.ui.settings.SettingsFragment
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.checkUpdates
import io.ktor.client.plugins.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ContainerBinding

    private val file: File = File.createTempFile("app", "apk")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ContainerBinding.inflate(layoutInflater)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val settings = Settings(this@MainActivity)
                val loginData = settings.getLoginData()
                Singleton.login(loginData)
                checkUpdates(this@MainActivity, file)
            } catch (e: ResponseException) {
                errorDialog(this@MainActivity, e.message ?: "")
            }

            withContext(Dispatchers.Main) {
                setContentView(binding.root)
                if (savedInstanceState == null) {
                    supportFragmentManager
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .add(binding.container.id, MainScreenFragment())
                        .commit()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        file.delete()
        CoroutineScope(Dispatchers.IO).launch {
            Singleton.requests.logout()
        }
    }

    override fun settings() {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(binding.container.id, SettingsFragment())
            .commit()
    }
}