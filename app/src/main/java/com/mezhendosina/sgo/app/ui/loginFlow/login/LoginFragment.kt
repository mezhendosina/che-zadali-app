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

package com.mezhendosina.sgo.app.ui.loginFlow.login

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentLoginBinding
import com.mezhendosina.sgo.app.utils.findTopNavController
import com.mezhendosina.sgo.app.utils.hideAnimation
import com.mezhendosina.sgo.app.utils.showAnimation

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    companion object {
        const val ARG_SCHOOL_ID = "schoolId"
    }

    private var schoolId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLoginBinding.bind(view)
        schoolId = requireArguments().getInt(ARG_SCHOOL_ID)
        binding.selectedSchool.text = viewModel.findSchool(schoolId!!).name
        binding.selectedSchoolCard.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.loginButton.setOnClickListener { loginClickListener() }

        observeErrors()
        observeLoading()
    }

    private fun observeErrors() {
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
        }
    }


    private fun observeLoading() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) showAnimation(binding.progressIndicator)
            else hideAnimation(binding.progressIndicator, View.INVISIBLE)
        }
    }

    private fun loginClickListener() {
        val login = binding.loginEditText.text
        val password = binding.passwordEditText.text

        if (login.isNullOrEmpty()) binding.loginTextField.error = "Не введен логин"
        if (password.isNullOrEmpty()) binding.passwordTextField.error = "Не введен пароль"

        if (!binding.loginEditText.text.isNullOrEmpty() && !binding.passwordEditText.text.isNullOrEmpty()) {
            viewModel.login(
                requireContext(),
                schoolId!!,
                login.toString(),
                password.toString(),
                findTopNavController()
            )
            val imm =
                requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
        }
    }
}