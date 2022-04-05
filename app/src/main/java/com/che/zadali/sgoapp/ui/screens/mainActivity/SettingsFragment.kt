package com.che.zadali.sgoapp.ui.screens.mainActivity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.che.zadali.sgoapp.R
import com.che.zadali.sgoapp.databinding.SettingsFragmentBinding
import com.che.zadali.sgoapp.ui.navigator
import com.che.zadali.sgoapp.ui.viewModels.SettingsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.MaterialFadeThrough


class SettingsFragment(private val supportActionBar: androidx.appcompat.app.ActionBar?) :
    Fragment() {
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

        val themes = arrayOf(
            getString(R.string.dark_theme),
            getString(R.string.light_theme),
            getString(R.string.same_as_system)
        )
        binding = SettingsFragmentBinding.inflate(inflater, container, false)
        supportActionBar?.setTitle(R.string.settings)

        binding.changePassword.setOnClickListener { navigator().changePassword() }
        binding.changeControlQuestion.setOnClickListener { navigator().changeControlQuestion() }
        binding.logout.setOnClickListener { viewModel.logOut(inflater.context) }
        binding.themeCard.setOnClickListener { dialogBuilder(inflater.context, themes, binding) }
        //TODO clickable current year, and language

        viewModel.settingsData.observe(viewLifecycleOwner) { it ->
            binding.firstNameEditText.setText(it.firstName)
            binding.lastNameEditText.setText(it.lastName)
            binding.patronymicEditText.setText(it.thirdName)

            binding.settingsLoginEditText.setText(it.login)
            binding.phoneEditText.setText(it.phoneNum)
            binding.emailEditText.setText(it.email)

            binding.selectedTheme.text = getString(it.theme)
            binding.selectedLanguage.text = it.current_language
            binding.selectedCurrentYear.text = it.current_year

            binding.firstNameEditText.isEnabled = it.editableName
            binding.lastNameEditText.isEnabled = it.editableName
            binding.patronymicEditText.isEnabled = it.editableName

            binding.settingsLoginEditText.isEnabled = it.editableLogin
            binding.phoneEditText.isEnabled = it.editablePhoneNum
            binding.emailEditText.isEnabled = it.editableEmail

            //todo birthday

        }

        return binding.root
    }

    private fun dialogBuilder(
        context: Context,
        items: Array<String>,
        binding: SettingsFragmentBinding
    ) {
        MaterialAlertDialogBuilder(context, R.style.AlertDialog)
            .setTitle(getString(R.string.choose_theme))
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }
            .setItems(items) { dialog, item ->
                binding.selectedTheme.setText(
                    when (item) {
                        0 -> R.string.dark_theme
                        1 -> R.string.light_theme
                        2 -> R.string.same_as_system
                        else -> R.string.same_as_system
                    }
                )
                dialog.cancel()
                viewModel.changeTheme(
                    when (item) {
                        0 -> AppCompatDelegate.MODE_NIGHT_YES
                        1 -> AppCompatDelegate.MODE_NIGHT_NO
                        2 -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                        else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    },
                    context
                )
            }

            .show()
    }
}