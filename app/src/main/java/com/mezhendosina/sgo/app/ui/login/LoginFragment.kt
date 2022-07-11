package com.mezhendosina.sgo.app.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    companion object {
        const val ARG_SCHOOL_ID = "schoolId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLoginBinding.bind(view)
        val schoolId = requireArguments().getInt(ARG_SCHOOL_ID)


        binding.selectedSchool.text = viewModel.findSchool(schoolId)?.school
        binding.selectedSchoolCard.setOnClickListener {
           findNavController().popBackStack()
        }

        binding.loginButton.setOnClickListener {
            if (binding.loginEditText.text.isNullOrEmpty()) {
                binding.loginTextField.error = "Не введен логин"
            }
            if (binding.passwordEditText.text.isNullOrEmpty()) {
                binding.passwordTextField.error = "Не введен пароль"
            }
            if (!binding.loginEditText.text.isNullOrEmpty() && !binding.passwordEditText.text.isNullOrEmpty()) {
                viewModel.onClickLogin(
                    binding,
                    requireContext(),
                    requireActivity(),
                    schoolId
                )

            }
        }

    }
}