package com.che.zadali.sgoapp.ui.screens.mainActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.che.zadali.sgoapp.databinding.SettingsFragmentBinding
import com.che.zadali.sgoapp.ui.navigator
import com.che.zadali.sgoapp.ui.viewModels.SettingsViewModel
import com.google.android.material.transition.MaterialFadeThrough

class SettingsFragment : Fragment() {
    private lateinit var binding: SettingsFragmentBinding
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reenterTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.loadSettings(inflater.context)
        binding = SettingsFragmentBinding.inflate(inflater, container, false)

        binding.changePassword.setOnClickListener { navigator().changePassword() }
        binding.changeControlQuestion.setOnClickListener { navigator().changeControlQuestion() }
        binding.logout.setOnClickListener { viewModel.logOut(inflater.context) }

        viewModel.currentYear.observe(viewLifecycleOwner) {
            binding.selectedCurrentYear.text = it
        }
        viewModel.language.observe(viewLifecycleOwner) {
            binding.selectedLanguage.text = it
        }
        viewModel.theme.observe(viewLifecycleOwner) {
            binding.selectedTheme.text = it
        }

        return binding.root
    }
}