package com.mezhendosina.sgo.app.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.activities.LoginActivity
import com.mezhendosina.sgo.app.databinding.SettingsFragmentBinding
import com.mezhendosina.sgo.app.ui.settings.changeControlQuestion.ChangeControlQuestionFragment
import com.mezhendosina.sgo.app.ui.settings.changeControlQuestion.ChangeControlQuestionViewModel
import com.mezhendosina.sgo.data.DateManipulation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsFragment : Fragment(R.layout.settings_fragment) {

    val viewModel: SettingsViewModel by viewModels()
    private lateinit var binding: SettingsFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)

        viewModel.getMySettings(arguments)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SettingsFragmentBinding.bind(view)

        when (AppCompatDelegate.getDefaultNightMode()) {
            1 -> binding.lightTheme.isChecked = true
            2 -> binding.darkTheme.isChecked = true
            else -> binding.sameAsSystem.isChecked = true
        }

        viewModel.loadProfilePhoto(
            requireContext(),
            binding.userPhoto
        )

        viewModel.mySettingsResponse.observe(viewLifecycleOwner) {
            binding.userName.text = "${it.lastName} ${it.firstName} ${it.middleName}"
            binding.userLogin.text = it.loginName
            binding.birthday.editText?.setText(DateManipulation(it.birthDate).dateFormatter())
            binding.phoneNumber.editText?.setText(it.mobilePhone)
            binding.email.editText?.setText(it.email)
            binding.switchNumberVisible.isChecked = it.userSettings.showMobilePhone
        }

        binding.changePhotoButton.setOnClickListener {
            Snackbar.make(
                binding.root,
                "Пока так нельзя, но скоро можно будет :)",
                Snackbar.LENGTH_LONG
            ).show()
        }

        binding.phoneNumber.editText?.addTextChangedListener {
            viewModel.phoneNumber.value = it.toString()

        }
        binding.switchNumberVisible.setOnCheckedChangeListener { _, isChecked ->
            viewModel.phoneNumberVisibility.value = isChecked
        }
        binding.email.editText?.addTextChangedListener {
            viewModel.email.value = it.toString()
        }

        binding.changePassword.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment4_to_changePasswordFragment)
        }
        binding.changeControlQuestion.setOnClickListener {
            findNavController().navigate(
                R.id.action_settingsFragment4_to_changeControlQuestionFragment,
                bundleOf(
                    ChangeControlQuestionFragment.SELECTED_QUEStION to viewModel.mySettingsResponse.value?.userSettings?.recoveryQuestion,
                    ChangeControlQuestionFragment.ANSWER to viewModel.mySettingsResponse.value?.userSettings?.recoveryAnswer
                )
            )
        }

        binding.cacheSize.text =
            "Объем кэша: ${(viewModel.calculateCache(requireContext())).toDouble()}"
        binding.clearCacheCard.setOnClickListener {}

        binding.changeThemeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.changeTheme(checkedId, requireContext())
        }
        binding.logoutButton.setOnClickListener { viewModel.logout(requireContext()) }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.sendSettings(requireContext())
    }

}
