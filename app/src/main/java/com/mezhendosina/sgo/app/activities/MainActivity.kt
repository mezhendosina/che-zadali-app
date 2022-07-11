package com.mezhendosina.sgo.app.activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ContainerMainActivityBinding
import com.mezhendosina.sgo.app.ui.errorDialog
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.weekEnd
import com.mezhendosina.sgo.data.weekStart
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        CoroutineScope(Dispatchers.IO).launch {
            try {
                singleton.requests.login(Settings(this@MainActivity).getLoginData())
                val currentWeek = singleton.currentWeek
                val a = singleton.requests.diaryInit(singleton.at)
                singleton.requests.diary(
                    singleton.at,
                    a.students[0].studentId,
                    weekEnd(currentWeek),
                    weekStart(currentWeek),
                    singleton.currentYearId
                )
            } catch (e: ResponseException) {
                withContext(Dispatchers.Main) {
                    errorDialog(this@MainActivity, e.response.body())
                }
            }
            withContext(Dispatchers.Main) {
            }
        }

        super.onRestart()
    }

    override fun onStop() {
        CoroutineScope(Dispatchers.IO).launch {
            Singleton.requests.logout()
            withContext(Dispatchers.Main) {
            }
        }
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
