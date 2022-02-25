package com.che.zadali.sgoapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.che.zadali.sgoapp.R
import com.che.zadali.sgoapp.databinding.MainContainerBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainContainerBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val bottomNavigationView = binding.bottomBar

        val navController = findNavController(R.id.fragmentContainerView)
        val appBarConfig = AppBarConfiguration(setOf(R.id.main, R.id.journal))

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfig)
        bottomNavigationView.setupWithNavController(navController)
        supportActionBar?.setDisplayShowCustomEnabled(true)

        navController.addOnDestinationChangedListener { _, b, _ ->
            when (b.label) {
                getString(R.string.mainTab) -> {
                    binding.toolbar.setTitle(R.string.main)
                }
                getString(R.string.journal) -> {
                    binding.toolbar.setTitle(R.string.journal)
                }
                getString(R.string.other) -> {
                    binding.toolbar.setTitle(R.string.other)
                }
            }
        }
    }
}