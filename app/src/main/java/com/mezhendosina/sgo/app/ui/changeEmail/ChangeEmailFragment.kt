package com.mezhendosina.sgo.app.ui.changeEmail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentChangeEmailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChangeEmailFragment : Fragment(R.layout.fragment_change_email) {

    lateinit var binding: FragmentChangeEmailBinding

    private val viewModel: ChangeEmailViewModel by viewModels()

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

        binding = FragmentChangeEmailBinding.bind(view)

        binding.textInputLayout.editText?.setText(arguments?.getString(EMAIL))

        binding.fab.setOnClickListener {
            viewModel.changeEmail(binding.textInputLayout.editText?.text.toString())
            findNavController().navigateUp()
        }
    }

    companion object {
        const val EMAIL = "email"
    }
}