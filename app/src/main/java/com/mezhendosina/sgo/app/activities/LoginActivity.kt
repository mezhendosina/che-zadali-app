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
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ContainerLoginBinding
import com.mezhendosina.sgo.app.utils.setupStatusBar

class LoginActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private var binding: ContainerLoginBinding? = null

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (navController.currentDestination?.id == navController.graph.startDestinationId) {
                finish()
            } else {
                navController.popBackStack()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        binding = ContainerLoginBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setSupportActionBar(binding!!.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHost.navController
        setupStatusBar(binding!!.root)
        binding!!.toolbar.setupWithNavController(navController)
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}
