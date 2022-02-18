package com.che.zadali.sgoapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.che.zadali.sgoapp.R
import com.che.zadali.sgoapp.databinding.MainContainerBinding
import com.che.zadali.sgoapp.databinding.NoMainContainerBinding
import com.che.zadali.sgoapp.navigators.Navigator
import com.che.zadali.sgoapp.ui.screens.loginActivity.LoginFragment
import com.che.zadali.sgoapp.ui.screens.mainActivity.SettingsFragment

class MainActivity : AppCompatActivity(), Navigator {
    private lateinit var binding: MainContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainContainerBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val bottomNavigationView = binding.bottomBar

        val navController = findNavController(R.id.fragmentContainerView)
        val appBarConfig = AppBarConfiguration(setOf(R.id.main, R.id.journal))

        setSupportActionBar(binding.supportActionBar)
        setupActionBarWithNavController(navController, appBarConfig)
        bottomNavigationView.setupWithNavController(navController)
        supportActionBar?.hide()

        navController.addOnDestinationChangedListener { _, b, _ ->
            binding.mainToolbar.setNavigationOnClickListener {

            }
            when (b.label) {
                getString(R.string.mainTab) -> {
                    binding.mainToolbar.setTitle(R.string.main)
                }
                getString(R.string.journal) -> {
                    binding.mainToolbar.setTitle(R.string.journal)
                }
                getString(R.string.other) -> {
                    binding.mainToolbar.setTitle(R.string.other)
                }
            }
        }
    }

    override fun goBack() {
        TODO("Not yet implemented")
    }

    override fun chooseSchool(typedSchool: String?) {
        TODO("Not yet implemented")
    }

    override fun login(schoolId: Int, typedSchool: String) {
        TODO("Not yet implemented")
    }

    override fun journal() {
        TODO("Not yet implemented")
    }

    override fun main() {
        TODO("Not yet implemented")
    }

    override fun settings() {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .replace(R.id.container, SettingsFragment())
            .commit()
    }

    override fun forum() {
        TODO("Not yet implemented")
    }

    override fun messages() {
        TODO("Not yet implemented")
    }

}