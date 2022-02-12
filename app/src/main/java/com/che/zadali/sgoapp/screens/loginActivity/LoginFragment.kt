package com.che.zadali.sgoapp.screens.loginActivity

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.che.zadali.sgoapp.R
import com.che.zadali.sgoapp.databinding.LoginFragmentBinding
import com.che.zadali.sgoapp.screens.factory
import com.che.zadali.sgoapp.screens.navigator
import com.che.zadali.sgoapp.screens.viewModels.loginAcitivity.LoginViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: LoginFragmentBinding
    private val viewModel: LoginViewModel by viewModels { factory() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadSchool(requireArguments().getInt(ARG_SCHOOL_ID))
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fade)
        exitTransition = inflater.inflateTransition(R.transition.fade)
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
        binding.school.setOnClickListener { navigator().goBack() }
        binding.FAB.setOnClickListener { viewModel.onClick() }

        startPostponedEnterTransition()
        return binding.root

    }

    companion object {

        private const val ARG_SCHOOL_ID = "ARG_SCHOOL_ID"

        fun newInstance(id: Int): Fragment {
            val fragment = LoginFragment()
            fragment.arguments = bundleOf(ARG_SCHOOL_ID to id)
            return fragment
        }
    }
}