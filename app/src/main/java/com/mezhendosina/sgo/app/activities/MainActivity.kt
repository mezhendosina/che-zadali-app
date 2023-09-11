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
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager
import com.google.android.material.transition.MaterialFadeThrough
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.databinding.ContainerMainActivityBinding
import com.mezhendosina.sgo.app.utils.errorDialog
import com.mezhendosina.sgo.app.utils.setupStatusBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ContainerMainActivityBinding
    private var navController: NavController? = null

    private val viewModel: MainViewModel by viewModels()

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentCreated(
            fm: FragmentManager,
            f: Fragment,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentCreated(fm, f, savedInstanceState)
            if (f.findNavController() != navController) navController = f.findNavController()
            println("start destination ${navController?.graph?.startDestDisplayName}")

        }
    }
    private lateinit var analytics: FirebaseAnalytics

//    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
//        override fun handleOnBackPressed() {
//            if (navController?.currentDestination?.id == R.id.journalFragment) {
//                finish()
//            } else {
//                navController?.navigateUp()
//            }
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ContainerMainActivityBinding.inflate(layoutInflater)
        analytics = Firebase.analytics
        analytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundleOf())
        binding.splashScreen.root.visibility = View.VISIBLE
        binding.container.visibility = View.GONE

        setContentView(binding.root)
        CoroutineScope(Dispatchers.IO).launch {

            withContext(Dispatchers.Main) {
                viewModel.errorMessage.observe(this@MainActivity) {
                    errorDialog(this@MainActivity, it)
                }
            }
            viewModel.login(this@MainActivity)

            withContext(Dispatchers.Main) {
                TransitionManager.beginDelayedTransition(
                    binding.root,
                    MaterialFadeThrough()
                )
                binding.splashScreen.root.visibility = View.GONE
                binding.container.visibility = View.VISIBLE
                setupStatusBar(binding.root)

            }
        }
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)
//        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    override fun onRestart() {
        super.onRestart()
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.login(this@MainActivity)
        }
    }

    override fun onStop() {
        viewModel.logout()
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
        Singleton.journalTabsLayout = null
    }


    override fun onBackPressed() {
        if (navController?.currentDestination?.id == navController?.graph?.startDestinationId) super.onBackPressed()
        else navController?.popBackStack()
    }
}