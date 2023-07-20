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

package com.mezhendosina.sgo.app.ui.loginFlow.gosuslugiResult

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.activities.MainActivity
import com.mezhendosina.sgo.app.databinding.FragmentGosuslugiResultBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GosuslugiResultFragment : Fragment(R.layout.fragment_gosuslugi_result) {


    private val viewModel by viewModels<GosuslugiResultViewModel>()
    private var binding: FragmentGosuslugiResultBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.auth(
                requireContext(),
                arguments?.getString(LOGIN_STATE)!!,
                arguments?.getString(USER_ID)!!
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGosuslugiResultBinding.bind(view)

        observeLoggedIn()
    }

    private fun observeLoggedIn() {
        viewModel.loggedIn.observe(viewLifecycleOwner) {
            if (it == true) {
                binding!!.result.setImageResource(R.drawable.ic_done_flat)
                binding!!.resultText.text = getString(R.string.logged_in_via_esia)
                CoroutineScope(Dispatchers.IO).launch {
                    delay(3000)
                    withContext(Dispatchers.Main) {
                        val intent = Intent(context, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        ContextCompat.startActivity(requireContext(), intent, null)
                    }
                }
            } else if (it == false) {
                binding!!.result.setImageResource(R.drawable.ic_error)
                binding!!.resultText.text = viewModel.error.value
            }
        }
    }

    companion object {
        const val LOGIN_STATE = "login_state"
        const val USER_ID = "user_id"
    }
}