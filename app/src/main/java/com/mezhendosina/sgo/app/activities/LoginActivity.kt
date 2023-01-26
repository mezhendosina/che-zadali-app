/*
 * Copyright 2023 Eugene Menshenin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mezhendosina.sgo.app.activities

import android.os.Bundle
import android.transition.TransitionManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ContainerLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    var binding: ContainerLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ContainerLoginBinding.inflate(layoutInflater)
        firebaseAnalytics = Firebase.analytics
        if (binding != null) {
            setContentView(binding!!.root)
            setSupportActionBar(binding!!.toolbar)

            val navHost =
                supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
            navController = navHost.navController

            binding!!.collapsingtoolbarlayout.setupWithNavController(
                binding!!.toolbar,
                navController
            )

            navController.addOnDestinationChangedListener { _, _, _ ->
                val materialSharedAxis = MaterialSharedAxis(MaterialSharedAxis.Y, true)
                TransitionManager.beginDelayedTransition(
                    binding!!.collapsingtoolbarlayout,
                    materialSharedAxis
                )
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()

        TransitionManager.endTransitions(binding?.collapsingtoolbarlayout)
        binding = null
    }


    override fun onBackPressed() {
        if (navController.currentDestination?.id == navController.graph.startDestinationId) {
            super.onBackPressed()
        } else {
            navController.navigateUp()
        }
    }
}
