package com.mezhendosina.sgo.app.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.Navigator
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ContainerBinding
import com.mezhendosina.sgo.app.ui.errorDialog
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
    private lateinit var navController: androidx.navigation.NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                println("ok")
                val settings = Settings(this@MainActivity)
                val loginData = settings.getLoginData()
                Singleton.login(loginData)
            } catch (e: ResponseException) {
                errorDialog(this@MainActivity, e.message ?: "")
            }
            println("ok1")
            withContext(Dispatchers.Main) {
                binding = ContainerBinding.inflate(layoutInflater)
                setContentView(binding.root)
                navController = supportFragmentManager.findFragmentById(R.id.container)?.findNavController()!!
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        CoroutineScope(Dispatchers.IO).launch {
            Singleton.requests.logout()
        }
    }

    override fun settings() {
        navController.navigate(R.id.settingsFragment)
    }

    override fun more(lessonId: Int, string: String) {
        navController.navigate(
            R.id.moreFragment,
            bundleOf("lessonId" to lessonId, "type" to string)
        )
    }


}