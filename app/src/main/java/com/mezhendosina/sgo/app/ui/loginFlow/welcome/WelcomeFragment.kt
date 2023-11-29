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

package com.mezhendosina.sgo.app.ui.loginFlow.welcome

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WelcomeFragment : Fragment(R.layout.fragment_welcome) {
    private var binding: FragmentWelcomeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWelcomeBinding.bind(view)

        binding!!.defaultLogin.setOnClickListener {
            findNavController().navigate(
                R.id.action_welcomeFragment_to_chooseRegionFragment,
                bundleOf(FROM_WELCOME to TO_SIGN_IN)
            )
        }
        binding!!.gosuslugiLogin.setOnClickListener {
            findNavController().navigate(
                R.id.action_welcomeFragment_to_chooseRegionFragment,
                bundleOf(FROM_WELCOME to TO_ESIA)
            )
        }
        if (!Singleton.welcomeShowed) {
            showAll()
            Singleton.welcomeShowed = true
        } else {
            with(binding!!) {
                hi.visibility = View.VISIBLE
                youIn.visibility = View.VISIBLE
                whyDisabled.visibility = View.VISIBLE
                about.visibility = View.VISIBLE
                gosuslugiLogin.visibility = View.VISIBLE
                defaultLogin.visibility = View.VISIBLE
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("a", "a")
        println(outState)

    }

    private fun showAll() {
        CoroutineScope(Dispatchers.Main).launch {
            val transition = MaterialSharedAxis(MaterialSharedAxis.Y, true)
            binding!!.hi.visibility = View.VISIBLE
            delay(1500)

            TransitionManager.beginDelayedTransition(binding!!.root, transition)
            binding!!.youIn.visibility = View.VISIBLE

            TransitionManager.beginDelayedTransition(binding!!.root, transition)
            binding!!.about.visibility = View.VISIBLE
            delay(500)

            showButton()
        }
    }

    private fun showButton() {
        val transition = MaterialFadeThrough()
        TransitionManager.beginDelayedTransition(binding!!.root, transition)
        binding!!.gosuslugiLogin.visibility = View.VISIBLE
        binding!!.defaultLogin.visibility = View.VISIBLE
        binding!!.whyDisabled.visibility = View.VISIBLE
    }


    companion object {
        const val FROM_WELCOME = "from_welcome"
        const val TO_ESIA = "esia"
        const val TO_SIGN_IN = "to_sign_in"
    }
}