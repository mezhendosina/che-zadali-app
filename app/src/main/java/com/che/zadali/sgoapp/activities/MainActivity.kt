package com.che.zadali.sgoapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.che.zadali.sgoapp.navigators.MainNavigator
import com.che.zadali.sgoapp.R
import com.che.zadali.sgoapp.databinding.MainContainerBinding
import com.che.zadali.sgoapp.ui.screens.mainActivity.MainFragment

class MainActivity : AppCompatActivity(), MainNavigator {
    private lateinit var binding: MainContainerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = binding.bottomBar

        val navController = findNavController(R.id.fragmentContainerView)
        val appBarConfig = AppBarConfiguration(setOf(R.id.main, R.id.journal))

        setSupportActionBar(binding.mainToolbar)
        setupActionBarWithNavController(navController, appBarConfig)
        bottomNavigationView.setupWithNavController(navController)
    }

    override fun goBack() {
        TODO("Not yet implemented")
    }

    override fun journal() {

    }

    override fun main() {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, MainFragment())
            .commit()
    }

    override fun settings() {
        TODO("Not yet implemented")
    }

    override fun forum() {
        TODO("Not yet implemented")
    }

    override fun messages() {
        TODO("Not yet implemented")
    }
}