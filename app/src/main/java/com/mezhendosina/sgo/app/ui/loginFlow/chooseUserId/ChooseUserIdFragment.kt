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

package com.mezhendosina.sgo.app.ui.loginFlow.chooseUserId

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentChooseUserIdBinding
import com.mezhendosina.sgo.app.ui.loginFlow.gosuslugiResult.GosuslugiResultFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseUserIdFragment : Fragment(R.layout.fragment_choose_user_id) {

    private lateinit var binding: FragmentChooseUserIdBinding
    private val viewModel: ChooseUserIdViewModel by viewModels()

    val adapter = UserIdAdapter {
        val loginState = arguments?.getString(GosuslugiResultFragment.LOGIN_STATE)
        if (loginState == null) {
            viewModel.login(requireContext(), it)
        } else {
            findNavController().navigate(
                R.id.action_chooseUserIdFragment_to_gosuslugiResult,
                bundleOf(
                    GosuslugiResultFragment.LOGIN_STATE to loginState,
                    GosuslugiResultFragment.USER_ID to it.userId
                )
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentChooseUserIdBinding.bind(view)

        observeUsers()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeUsers() {
        viewModel.usersId.observe(viewLifecycleOwner) {
            adapter.users = it
        }
    }
}
