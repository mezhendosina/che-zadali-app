package com.mezhendosina.sgo.app.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.transition.MaterialSharedAxis
import com.mezhendosina.sgo.app.databinding.SettingsFragmentBinding

class SettingsFragment : Fragment() {

    val viewModel: SettingsViewModel by viewModels()
    private lateinit var binding: SettingsFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)

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