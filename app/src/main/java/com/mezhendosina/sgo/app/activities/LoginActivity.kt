package com.mezhendosina.sgo.app.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ContainerLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAnalytics = Firebase.analytics

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
