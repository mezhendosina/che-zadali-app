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

package com.mezhendosina.sgo.app.ui.settingsFlow.changePhone

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentChangePhoneNumberBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChangePhoneFragment : Fragment(R.layout.fragment_change_phone_number) {

    lateinit var binding: FragmentChangePhoneNumberBinding

    private val viewModel: ChangePhoneViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getSettings()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val phone = arguments?.getString(PHONE_NUMBER)

        binding = FragmentChangePhoneNumberBinding.bind(view)
        binding.editText.setText(phone)
        binding.textInputLayout.editText?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            // from stackoverflow
            override fun afterTextChanged(editable: Editable) {
                val text = binding.textInputLayout.editText?.text.toString()
                val textLength = binding.textInputLayout.editText?.text?.length

                if (text.endsWith("-") || text.endsWith(" ")) {
                    return
                }

                if (textLength == 1) {
                    if (!text.contains("+")) {
                        setText(StringBuilder(text).insert(text.length - 1, "+").toString())
                    }
                } else if (textLength == 3) {
                    if (!text.contains("(")) {
                        setText(StringBuilder(text).insert(text.length - 1, " (").toString())
                    }
                } else if (textLength == 8) {
                    if (!text.contains(")")) {
                        setText(StringBuilder(text).insert(text.length - 1, ") ").toString())
                    }
                } else if (textLength == 13) {
                    if (!text.contains("-")) {
                        setText(StringBuilder(text).insert(text.length - 1, "-").toString())
                    }
                }
            }

            private fun setText(text: String) {
                binding.textInputLayout.editText?.removeTextChangedListener(this)
                binding.textInputLayout.editText?.text?.length?.let {
                    binding.textInputLayout.editText?.editableText?.replace(
                        0,
                        it,
                        text
                    )
                }
                binding.textInputLayout.editText?.setSelection(text.length)
                binding.textInputLayout.editText?.addTextChangedListener(this)
            }
        })

        binding.fab.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.changePhone(binding.textInputLayout.editText?.text.toString())
                findNavController().navigate(
                    R.id.action_changePhoneFragment_to_settingsFragment,
                )
            }
        }
    }

    companion object {
        const val PHONE_NUMBER = "phone_number"
        const val PHONE_VISIBILITY = "phone_visibility"
    }
}

