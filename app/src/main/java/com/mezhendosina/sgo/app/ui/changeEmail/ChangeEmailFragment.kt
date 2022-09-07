package com.mezhendosina.sgo.app.ui.changeEmail

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentChangeEmailBinding

class ChangeEmailFragment : Fragment(R.layout.fragment_change_email) {

    lateinit var binding: FragmentChangeEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentChangeEmailBinding.bind(view)

        binding.textInputLayout.editText?.setText(arguments?.getString(EMAIL))

        binding.fab.setOnClickListener {
            val emailBundle = bundleOf(EMAIL to binding.textInputLayout.editText?.text.toString())
            findNavController().navigate(
                R.id.action_changeEmailFragment_to_settingsFragment2,
                emailBundle
            )
        }
    }

    companion object {
        const val EMAIL = "email"
    }
}