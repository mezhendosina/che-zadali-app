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

package com.mezhendosina.sgo.app.ui.changePassword

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentChangePasswordBinding
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.toMD5
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChangePasswordFragment : Fragment(R.layout.fragment_change_password) {

    private lateinit var binding: FragmentChangePasswordBinding
    private var currentPassword = ""

    private val viewModel by viewModels<ChangePasswordViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)


        CoroutineScope(Dispatchers.Main).launch {
            currentPassword = Settings(requireContext()).getLoginData().PW
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentChangePasswordBinding.bind(view)


        binding.currentPasswordInputLayout.editText?.addTextChangedListener {
            if (it != null) {
                if (it.isEmpty()) {
                    binding.currentPasswordInputLayout.apply {
                        error = "Введите старый пароль"
                        isErrorEnabled = true
                    }
                } else if (it.toString().toMD5() != currentPassword) {
                    binding.currentPasswordInputLayout.apply {
                        error = "Неверный пароль"
                        isErrorEnabled = true
                    }
                } else binding.currentPasswordInputLayout.isErrorEnabled = false
            }
        }

        binding.newPasswordInputLayout.editText?.addTextChangedListener {
            if (it != null) {
                if (it.isEmpty()) {
                    binding.newPasswordInputLayout.apply {
                        error = "Новый пароль не может быть пустым"
                        isErrorEnabled = true
                    }
                } else if (it.length < 6) {
                    binding.newPasswordInputLayout.apply {
                        error = "Пароль должен содержать не менее 6 символов"
                        isErrorEnabled = true
                    }
                } else if (it.toString() == binding.currentPasswordInputLayout.editText?.text.toString()) {
                    binding.newPasswordInputLayout.apply {
                        error = "Новый пароль должен отличаться от старого"
                        isErrorEnabled = true
                    }
                } else binding.newPasswordInputLayout.isErrorEnabled = false
            }
        }

        binding.confirmPasswordInputLayout.editText?.addTextChangedListener {
            if (it != null) {
                if (it.isEmpty()) {
                    binding.confirmPasswordInputLayout.apply {
                        error = "Подтвердите пароль"
                        isErrorEnabled = true
                    }
                } else if (it.toString() != binding.newPasswordInputLayout.editText?.text.toString()) {
                    binding.confirmPasswordInputLayout.apply {
                        error = "Вы ввели разные пароли"
                        isErrorEnabled = true
                    }
                } else binding.confirmPasswordInputLayout.isErrorEnabled = false
            }
        }
        binding.fab.setOnClickListener {
            viewModel.changePassword(
                binding.currentPasswordInputLayout.editText?.text.toString(),
                binding.newPasswordInputLayout.editText?.text.toString(),
                requireContext()
            )
            CoroutineScope(Dispatchers.IO).launch {
                currentPassword = Settings(requireContext()).getLoginData().PW
            }
            findNavController().navigateUp()
        }
    }
}