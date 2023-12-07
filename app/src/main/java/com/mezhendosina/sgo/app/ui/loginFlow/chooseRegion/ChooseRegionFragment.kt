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

package com.mezhendosina.sgo.app.ui.loginFlow.chooseRegion

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.google.android.material.transition.MaterialSharedAxis
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentChooseRegionBinding
import com.mezhendosina.sgo.app.ui.loginFlow.setOnInsetChanges
import com.mezhendosina.sgo.app.ui.loginFlow.welcome.WelcomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseRegionFragment : Fragment(R.layout.fragment_choose_region) {

    private var binding: FragmentChooseRegionBinding? = null

    internal val viewModel: ChooseRegionViewModel by viewModels()
    private val adapter = ChooseRegionAdapter(
        object : OnRegionClickListener {
            override fun invoke(regionName: String) {
                viewModel.editSelectedRegion(regionName)
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter.selectedItem = viewModel.selectedRegion.value?.name ?: ""
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
//        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChooseRegionBinding.bind(view)


        binding!!.regionList.adapter = adapter
        binding!!.regionList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding!!.button.setOnClickListener {
            viewModel.setRegion(requireContext()) {
                val into = arguments?.getString(WelcomeFragment.FROM_WELCOME)
                findNavController().navigate(
                    when (into) {
                        WelcomeFragment.TO_ESIA -> R.id.action_chooseRegionFragment_to_gosuslugiFragment
                        WelcomeFragment.TO_SIGN_IN -> R.id.action_chooseRegionFragment_to_chooseSchoolFragment
                        else -> R.id.action_chooseRegionFragment_to_chooseSchoolFragment
                    }
                )
            }
        }
        binding!!.buttonView.setOnInsetChanges()

        observeRegions()
        observeSelectedRegion()
    }

    override fun onDestroy() {
        TransitionManager.endTransitions(binding!!.root)
        binding!!.regionList.adapter = null
        binding = null
        super.onDestroy()
    }

    private fun observeSelectedRegion() {
        viewModel.selectedRegion.observe(viewLifecycleOwner) {
            if (it != null) {
                val transition = MaterialSharedAxis(MaterialSharedAxis.Y, true)
                TransitionManager.beginDelayedTransition(binding!!.buttonView, transition)

                binding!!.button.visibility = View.VISIBLE
            }
        }
    }


    private fun observeRegions() {
        viewModel.regions.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.regions = it
            }
        }
    }

}