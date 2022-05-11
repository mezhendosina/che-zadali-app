package com.mezhendosina.sgo.app.ui.settings

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.SettingsFragmentBinding
import com.mezhendosina.sgo.app.ui.changeThemeAlertDialog

class SettingsFragment : Fragment() {

    val viewModel: SettingsViewModel by viewModels()
    private lateinit var binding: SettingsFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getCurrentTheme(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SettingsFragmentBinding.inflate(inflater, container, false)

        binding.logoutButton.setOnClickListener {
            viewModel.logout(requireContext())
        }

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

//        viewModel.currentTheme.observe(viewLifecycleOwner) {
//            binding.currentTheme.text = when (it) {
//                0 -> "Светлая"
//                1 -> "Темная"
//                2 -> "Как в системе"
//                else -> ""
//            }
//        }

//        binding.selectThemeCard.setOnClickListener {
//            changeThemeAlertDialog(
//                requireContext(),
//                arrayOf("Светлая", "Темная", "Как в системе"),
//                viewModel.currentTheme.value ?: 2
//            ) {
//                viewModel.changeTheme(requireContext(), it)
//            }
//        }

        return binding.root
    }


}