package com.che.zadali.sgoapp.ui.screens.mainActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.che.zadali.sgoapp.databinding.SettingsFragmentBinding
import com.che.zadali.sgoapp.ui.viewModels.SettingsViewModel

class SettingsFragment : Fragment() {
    private lateinit var binding: SettingsFragmentBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SettingsFragmentBinding.inflate(inflater, container, false)



        return binding.root
    }
}