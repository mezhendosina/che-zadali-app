package com.mezhendosina.sgo.app.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentSettingsBinding
import com.mezhendosina.sgo.app.ui.changeControlQuestion.ChangeControlQuestionFragment
import com.mezhendosina.sgo.app.ui.changeEmail.ChangeEmailFragment
import com.mezhendosina.sgo.app.ui.changePhone.ChangePhoneFragment
import com.mezhendosina.sgo.app.ui.chooseYearBottomSheet.ChooseYearBottomSheet
import com.mezhendosina.sgo.data.DateManipulation
import java.io.File

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var binding: FragmentSettingsBinding

    private val pickPhoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            it.data?.let { it1 -> viewModel.changePhoto(requireContext(), it1) }
        }
    val file: File = File.createTempFile("profile_photo", "tmp")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)

        viewModel.getMySettings(arguments)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)

        when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_NO -> binding.lightTheme.isChecked = true
            AppCompatDelegate.MODE_NIGHT_YES -> binding.darkTheme.isChecked = true
            else -> binding.sameAsSystem.isChecked = true
        }

        viewModel.loadProfilePhoto(
            requireContext(),
            binding.userPhoto
        )

        binding.phoneNumber.setOnClickListener {
            val phoneBundle =
                bundleOf(
                    ChangePhoneFragment.PHONE_NUMBER to viewModel.phoneNumber.value,
                    ChangePhoneFragment.PHONE_VISIBILITY to viewModel.phoneNumberVisibility.value
                )
            findNavController().navigate(
                R.id.action_settingsFragment_to_changePhoneFragment,
                phoneBundle
            )
        }

        binding.email.setOnClickListener {
            val emailBundle = bundleOf(
                ChangeEmailFragment.EMAIL to viewModel.email.value
            )
            findNavController().navigate(
                R.id.action_settingsFragment_to_changeEmailFragment,
                emailBundle
            )
        }

        binding.changePhotoButton.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            pickPhoto.launch(intent)
        }

        binding.changePassword.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment4_to_changePasswordFragment)
        }
        binding.changeControlQuestion.setOnClickListener {
            val userSettings = viewModel.mySettingsResponseEntity.value?.userSettings
            findNavController().navigate(
                R.id.action_settingsFragment4_to_changeControlQuestionFragment,
                bundleOf(
                    ChangeControlQuestionFragment.SELECTED_QUEStION to userSettings?.recoveryQuestion,
                    ChangeControlQuestionFragment.ANSWER to userSettings?.recoveryAnswer
                )
            )
        }

        binding.changeYear.setOnClickListener {
            if (viewModel.years.value != null)
                ChooseYearBottomSheet(
                    viewModel.years.value!!
                ).show(childFragmentManager, ChooseYearBottomSheet.TAG)
        }

        binding.cacheSize.text =
            "Объем кэша: ${(viewModel.calculateCache(requireContext())).toDouble()}"
        binding.clearCacheCard.setOnClickListener { TODO("clear cache") }

        binding.changeThemeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.changeTheme(checkedId, requireContext())
        }

        binding.aboutApp.setOnClickListener { TODO("Navigate to AboutAppFragment") }

        binding.logoutButton.setOnClickListener { viewModel.logout(requireContext()) }

        observeMySettings()
        observeYears()
    }

    private fun observeYears() {
        Singleton.currentYearId.observe(viewLifecycleOwner) { id ->
            viewModel.years.observe(viewLifecycleOwner) {
                binding.changeYearValue.text =
                    viewModel.years.value!!.first { it.id == id }.name.replace("(*) ", "")
            }
        }
    }

    private fun observeMySettings() {
        val regex = """(\d)(\d{3})(\d{3})(\d{2})(\d{2})""".toRegex()
        viewModel.mySettingsResponseEntity.observe(viewLifecycleOwner) {
            binding.userName.text = "${it.lastName} ${it.firstName} ${it.middleName}"
            binding.userLogin.text = it.loginName
            val birthday = DateManipulation(it.birthDate).dateFormatter()
            binding.birthdayDate.text = birthday

            if (it.mobilePhone.isNotEmpty()) binding.phoneNumberValue.text =
                regex.replace(it.mobilePhone, "+$1 ($2) $3-$4$5")
            else binding.phoneNumberValue.visibility = View.GONE

            if (it.email.isNotEmpty()) binding.emailValue.text = it.email
            else binding.emailValue.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.sendSettings(requireContext())
    }
}
