package com.mezhendosina.sgo.app.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentLoginBinding
import com.mezhendosina.sgo.app.ui.errorDialog
import com.mezhendosina.sgo.app.ui.hideAnimation
import com.mezhendosina.sgo.app.ui.showAnimation

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    companion object {
        const val ARG_SCHOOL_ID = "schoolId"
    }

    private var schoolId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLoginBinding.bind(view)
        schoolId = requireArguments().getInt(ARG_SCHOOL_ID)
        binding.selectedSchool.text = viewModel.findSchool(schoolId!!)?.school
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
            viewModel.login(requireContext(), schoolId!!, login.toString(), password.toString())
        }

    }

}