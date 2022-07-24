package com.mezhendosina.sgo.app.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ContainerLoginBinding
import com.mezhendosina.sgo.data.Settings
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ContainerLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHost.navController

        binding.collapsingtoolbarlayout.setupWithNavController(
            binding.toolbar,
            navController
        )
    }


    override fun onBackPressed() {
        if (navController.currentDestination?.id == navController.graph.startDestinationId) {
            super.onBackPressed()
        } else {
            navController.navigateUp()
        }
    }
}
