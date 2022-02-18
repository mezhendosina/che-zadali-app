package com.che.zadali.sgoapp.ui.screens.loginActivity

import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.che.zadali.sgoapp.R
import com.che.zadali.sgoapp.activities.MainActivity
import com.che.zadali.sgoapp.data.LoginData
import com.che.zadali.sgoapp.databinding.LoginFragmentBinding
import com.che.zadali.sgoapp.ui.factory
import com.che.zadali.sgoapp.ui.navigator
import com.che.zadali.sgoapp.ui.viewModels.LoginViewModel
import com.google.android.material.transition.MaterialSharedAxis

class LoginFragment : Fragment() {

    private lateinit var binding: LoginFragmentBinding
    private val viewModel: LoginViewModel by viewModels { factory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadSchool(requireArguments().getInt(ARG_SCHOOL_ID))
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        postponeEnterTransition()
        binding = LoginFragmentBinding.inflate(layoutInflater, container, false)

        viewModel.school.observe(viewLifecycleOwner) {
            binding.schoolTextView.text = "${it.city}, ${it.school} "
        }

        binding.toolbar.setNavigationOnClickListener { navigator().goBack() }
        binding.school.setOnClickListener {
            navigator().chooseSchool(
                requireArguments().getString(
                    ARG_TYPED_SCHOOl
                )
            )
        }
        binding.FAB.setOnClickListener {

            if (binding.passwordEditText.text.toString()
                    .isNotEmpty() && binding.loginEditText.text.toString().isNotEmpty()
            ) {
                val school = viewModel.school.value!!
                val loginData = LoginData(
                    binding.loginEditText.text.toString(),
                    binding.passwordEditText.text.toString(),
                    school.schoolId,
                    school.cityId,
                    school.provinceId
                )
                if (viewModel.onClick(inflater.context, loginData)) {
                    startActivity(Intent(inflater.context, MainActivity::class.java))
                }
            } else {
                if (binding.loginEditText.text.toString().isEmpty()) {
                    binding.loginEditLayout.isErrorEnabled = true
                    binding.loginEditLayout.error = getString(R.string.empty_login_error)
                }
                if (binding.passwordEditText.text.toString().isEmpty()) {
                    binding.passwordLayout.isErrorEnabled = true
                    binding.passwordLayout.error = getString(R.string.empty_password_error)
                }
            }

        }

        startPostponedEnterTransition()
        return binding.root

    }

    companion object {

        private const val ARG_SCHOOL_ID = "ARG_SCHOOL_ID"
        private const val ARG_TYPED_SCHOOl = "ARG_TYPED_SCHOOl"

        fun newInstance(id: Int, string: String): Fragment {
            val fragment = LoginFragment()
            fragment.arguments = bundleOf(ARG_SCHOOL_ID to id, ARG_TYPED_SCHOOl to string)
            return fragment
        }
    }
}