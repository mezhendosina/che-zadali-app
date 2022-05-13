package com.mezhendosina.sgo.app.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.LoginActivityBinding
import com.mezhendosina.sgo.data.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val settings = Settings(this)

        CoroutineScope(Dispatchers.IO).launch {
            if (settings.loggedIn.first()) {
                withContext(Dispatchers.Main) {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
            } else {
                withContext(Dispatchers.Main) {
                    val binding = LoginActivityBinding.inflate(layoutInflater)
                    setContentView(binding.root)
                    setSupportActionBar(binding.toolbar)

                    val navHostFragment =
                        supportFragmentManager.findFragmentById(binding.fragmentContainer.id) as NavHostFragment
                    val navController = navHostFragment.navController
                    appBarConfiguration = AppBarConfiguration(navController.graph)
                    binding.collapsingtoolbarlayout.setupWithNavController(binding.toolbar, navController, appBarConfiguration)

//                    navController.addOnDestinationChangedListener() { _, destination, _ ->
//                        if (destination.id == R.id.loginFragment) {
//                            supportActionBar?.title = "Login"
//
//                        }
//
//                    }
                }
            }
        }
    }
}
