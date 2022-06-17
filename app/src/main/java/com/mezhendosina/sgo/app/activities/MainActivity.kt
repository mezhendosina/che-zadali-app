package com.mezhendosina.sgo.app.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.MainActivityContainerBinding
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

    private lateinit var binding: MainActivityContainerBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        CoroutineScope(Dispatchers.IO).launch {
            val settings = Settings(this@MainActivity)
            try {
                val loginData = settings.getLoginData()
                Singleton.login(loginData)
            } catch (e: ResponseException) {
                withContext(Dispatchers.Main) {
                    errorDialog(this@MainActivity, e.message ?: "")
                }
            }

            withContext(Dispatchers.Main) {
                binding = MainActivityContainerBinding.inflate(layoutInflater)
                setContentView(binding.root)

//                settings.theme.collect(){
//                    when (it) {
//                        R.id.light_theme -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                        R.id.dark_theme -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//                        R.id.same_as_system -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
//                    }
//                }
            }
        }
    }

    private fun getMainNavGraphId(): Int = R.navigation.main_navigation

    override fun onRestart() {
        super.onRestart()
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
        }
    }

    override fun onStop() {
        super.onStop()
        CoroutineScope(Dispatchers.IO).launch {
            Singleton.requests.logout()
        }
    }

    private fun getRootNavController(): NavController {
        val navHost = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        return navHost.navController
    }
}
