package com.mezhendosina.sgo.app.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.MainActivityContainerBinding
import com.mezhendosina.sgo.app.ui.errorDialog
import com.mezhendosina.sgo.app.ui.main.MainFragment
import com.mezhendosina.sgo.data.Settings
import io.ktor.client.plugins.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val settings = Settings(this@MainActivity)
                val loginData = settings.getLoginData()
                Singleton.login(loginData)
            } catch (e: ResponseException) {
                errorDialog(this@MainActivity, e.message ?: "")
            }

            withContext(Dispatchers.Main) {
                binding = MainActivityContainerBinding.inflate(layoutInflater)
                setContentView(binding.root)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        CoroutineScope(Dispatchers.IO).launch {
            Singleton.requests.logout()
        }
    }
}