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

package com.mezhendosina.sgo.app.ui.loginFlow.chooseSchool


import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentChooseSchoolBinding
import com.mezhendosina.sgo.app.ui.loginFlow.login.LoginFragment
import com.mezhendosina.sgo.app.ui.loginFlow.setOnInsetChanges
import com.mezhendosina.sgo.app.uiEntities.SchoolUiEntity
import com.mezhendosina.sgo.app.utils.DividerItemDecoration
import com.mezhendosina.sgo.app.utils.hideAnimation
import com.mezhendosina.sgo.app.utils.showAnimation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChooseSchoolFragment : Fragment(R.layout.fragment_choose_school) {

    private var binding: FragmentChooseSchoolBinding? = null

    internal val viewModel: ChooseSchoolViewModel by viewModels()

    private val schoolAdapter = ChooseSchoolAdapter(object : OnSchoolClickListener {
        override fun invoke(p1: SchoolUiEntity) {
            viewModel.editSelectedItem(p1)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentChooseSchoolBinding.bind(view)

        if (!binding!!.schoolEditText.text.isNullOrEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                findSchool(binding!!.schoolEditText.text.toString())
            }
        }

        binding!!.schoolEditText.addTextChangedListener(afterTextChanged = {
            CoroutineScope(Dispatchers.IO).launch {
                delay(500)
                findSchool(it.toString())
            }
        })

        binding!!.loadError.retryButton.setOnClickListener {
            binding!!.schoolList.visibility = View.VISIBLE
        }

        binding!!.schoolList.adapter = schoolAdapter


        binding!!.schoolList.addItemDecoration(
            DividerItemDecoration(requireContext())
        )
        binding!!.schoolList.layoutManager = LinearLayoutManager(requireContext())

        binding!!.button.setOnClickListener {
            findNavController().navigate(
                R.id.action_chooseSchoolFragment_to_loginFragment,
                bundleOf(LoginFragment.ARG_SCHOOL_ID to viewModel.selectedItem.value?.id)
            )
        }
        binding!!.buttonView.setOnInsetChanges()

        observeSchools()
        observeErrors()
        observeSelectedItem()
        observeLoading()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


    private suspend fun findSchool(schoolName: String) {
        viewModel.findSchool(schoolName)
    }

    private fun observeSelectedItem() {
        viewModel.selectedItem.observe(viewLifecycleOwner) {
            if (it != null) {
                binding!!.button.visibility = View.VISIBLE
            }
        }
    }

    private fun observeSchools() {
        viewModel.schools.observe(viewLifecycleOwner) {
            schoolAdapter.schools = it
        }
    }

    private fun observeErrors() {
        viewModel.isError.observe(viewLifecycleOwner) {
            if (binding != null) {
                if (it) {
                    showAnimation(binding!!.loadError.root)
                    binding!!.schoolList.visibility = View.GONE
                }
            }
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            if (binding != null) {
                binding!!.loadError.errorDescription.text = it
            }
        }

    }

    private fun observeLoading() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (binding != null) {
                if (it) {
                    showAnimation(binding!!.progressIndicator)
                    binding!!.loadError.root.visibility = View.GONE
                } else {
                    hideAnimation(binding!!.progressIndicator, View.GONE)
//                    findSchool(binding!!.schoolEditText.text.toString())
                }
            }
        }
    }
}