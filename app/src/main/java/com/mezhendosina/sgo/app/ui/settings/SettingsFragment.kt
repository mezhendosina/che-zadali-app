package com.mezhendosina.sgo.app.ui.settings

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentSettingsBinding
import com.mezhendosina.sgo.app.ui.settings.changeControlQuestion.ChangeControlQuestionFragment
import com.mezhendosina.sgo.data.DateManipulation
import com.mezhendosina.sgo.data.uriFromFile
import java.io.File

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    val viewModel: SettingsViewModel by viewModels()
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
            1 -> binding.lightTheme.isChecked = true
            2 -> binding.darkTheme.isChecked = true
            else -> binding.sameAsSystem.isChecked = true
        }

        viewModel.loadProfilePhoto(
            requireContext(),
            binding.userPhoto
        )

        val regex = """(\d)(\d{3})(\d{3})(\d{2})(\d{2})""".toRegex()
        viewModel.mySettingsResponse.observe(viewLifecycleOwner) {
            binding.userName.text = "${it.lastName} ${it.firstName} ${it.middleName}"
            binding.userLogin.text = it.loginName
            binding.birthday.editText?.setText(DateManipulation(it.birthDate).dateFormatter())
            binding.phoneNumber.editText?.setText(
                regex.replace(
                    it.mobilePhone,
                    "+$1 ($2) $3-$4$5"
                )
            )
            binding.email.editText?.setText(it.email)
            binding.switchNumberVisible.isChecked = it.userSettings.showMobilePhone
        }

        binding.changePhotoButton.setOnClickListener {
            //TODO change photo button
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            pickPhoto.launch(intent)
//            Snackbar.make(
//                binding.root,
//                "Пока так нельзя, но скоро можно будет :)",
//                Snackbar.LENGTH_LONG
//            ).show()
        }

        // Нагло украдено со StackOverFlow
        binding.phoneNumber.editText?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.phoneNumber.value = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(editable: Editable) {
                val text = binding.phoneNumber.editText?.text.toString()
                val textLength = binding.phoneNumber.editText?.text?.length

                if (text.endsWith("-") || text.endsWith(" ")) {
                    return
                }

                if (textLength == 1) {
                    if (!text.contains("+")) {
                        setText(StringBuilder(text).insert(text.length - 1, "+").toString())
                    }
                } else if (textLength == 3) {
                    if (!text.contains("(")) {
                        setText(StringBuilder(text).insert(text.length - 1, " (").toString())
                    }
                } else if (textLength == 8) {
                    if (!text.contains(")")) {
                        setText(StringBuilder(text).insert(text.length - 1, ") ").toString())
                    }
                } else if (textLength == 13) {
                    if (!text.contains("-")) {
                        setText(StringBuilder(text).insert(text.length - 1, "-").toString())
                    }
                }
            }

            private fun setText(text: String) {
                binding.phoneNumber.editText?.removeTextChangedListener(this)
                binding.phoneNumber.editText?.text?.length?.let {
                    binding.phoneNumber.editText?.editableText?.replace(
                        0,
                        it,
                        text
                    )
                }
                binding.phoneNumber.editText?.setSelection(text.length)
                binding.phoneNumber.editText?.addTextChangedListener(this)
            }
        })

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
            val userSettings = viewModel.mySettingsResponse.value?.userSettings
            findNavController().navigate(
                R.id.action_settingsFragment4_to_changeControlQuestionFragment,
                bundleOf(
                    ChangeControlQuestionFragment.SELECTED_QUEStION to userSettings?.recoveryQuestion,
                    ChangeControlQuestionFragment.ANSWER to userSettings?.recoveryAnswer
                )
            )
        }

        binding.cacheSize.text =
            "Объем кэша: ${(viewModel.calculateCache(requireContext())).toDouble()}"
        binding.clearCacheCard.setOnClickListener { TODO("clear cache") }

        binding.changeThemeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.changeTheme(checkedId, requireContext())
        }

        binding.aboutApp.setOnClickListener { TODO("Navigate to AboutAppFragment") }

        binding.logoutButton.setOnClickListener { viewModel.logout(requireContext()) }
    }



    override fun onDestroy() {
        super.onDestroy()
        viewModel.sendSettings(requireContext())
    }
}
