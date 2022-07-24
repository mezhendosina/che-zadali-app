package com.mezhendosina.sgo.app.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.databinding.ContainerMainActivityBinding
import com.mezhendosina.sgo.app.ui.errorDialog
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.layouts.Error
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.util.network.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ContainerMainActivityBinding
    private var navController: NavController? = null

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentCreated(
            fm: FragmentManager,
            f: Fragment,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentCreated(fm, f, savedInstanceState)
            if (f.findNavController() != navController) navController = f.findNavController()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            val settings = Settings(this@MainActivity)
            try {
                val loginData = settings.getLoginData()
                Singleton.login(loginData)
            } catch (response: ResponseException) {
                withContext(Dispatchers.Main) {
                    errorDialog(this@MainActivity, response.response.body() ?: "")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    errorDialog(this@MainActivity, e.message ?: "")
                }
            }
            withContext(Dispatchers.Main) {
                binding = ContainerMainActivityBinding.inflate(layoutInflater)
                setContentView(binding.root)
            }
        }
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)
    }

    override fun onRestart() {
        val singleton = Singleton
        runBlocking {
            try {
                singleton.login(Settings(this@MainActivity).getLoginData())
            } catch (e: ResponseException) {
                withContext(Dispatchers.Main) {
                    errorDialog(this@MainActivity, e.response.body<Error>().message)
                }
            } catch (e: UnresolvedAddressException) {
                withContext(Dispatchers.Main) {
                    errorDialog(this@MainActivity, "Похоже, что нету итернета :(")
                }
            }
        }
        super.onRestart()
    }

    override fun onStop() {
        CoroutineScope(Dispatchers.IO).launch { Singleton.requests.logout() }
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
    }


    override fun onBackPressed() {
        if (navController?.currentDestination?.id != navController?.graph?.startDestinationId) navController?.navigateUp()
        else super.onBackPressed()
    }
}
