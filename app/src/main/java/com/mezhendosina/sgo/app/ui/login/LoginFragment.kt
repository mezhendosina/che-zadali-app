package com.mezhendosina.sgo.app.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mezhendosina.sgo.app.databinding.LoginFragmentBinding

class LoginFragment : Fragment() {

    private lateinit var binding: LoginFragmentBinding

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginFragmentBinding.inflate(inflater, container, false)


        binding.fab.setOnClickListener {
            if (binding.loginEditText.text.isNullOrEmpty()) {
                binding.loginTextField.error = "Не введен логин"
            }
            if (binding.passwordEditText.text.isNullOrEmpty()) {
                binding.passwordTextField.error = "Не введен пароль"
            }
            if (!binding.loginEditText.text.isNullOrEmpty() && !binding.passwordEditText.text.isNullOrEmpty()) {
                viewModel.onClickLogin(binding, requireContext())
            }
        }


        return binding.root
    }
}