/*
 * Copyright 2023 Eugene Menshenin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mezhendosina.sgo.app.ui.settingsFlow

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentSettingsBinding
import com.mezhendosina.sgo.app.ui.settingsFlow.changeControlQuestion.ChangeControlQuestionFragment
import com.mezhendosina.sgo.app.ui.settingsFlow.changeEmail.ChangeEmailFragment
import com.mezhendosina.sgo.app.ui.settingsFlow.changePhone.ChangePhoneFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var binding: FragmentSettingsBinding

//    private val checkNotificationsPermission =
//        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
//            if (it && viewModel.enableGradeNotifications.value == false) {
//                CoroutineScope(Dispatchers.IO).launch {
////                    viewModel.changeGradeNotifications(requireContext())
//                }
//            } else if (!it) {
//                Snackbar.make(
//                    binding.root,
//                    "Нет разрешения на отправку уведомлений уведомления",
//                    Snackbar.LENGTH_LONG
//                ).show()
//            }
//        }

    val file: File = File.createTempFile("profile_photo", "tmp")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)

    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getMySettings(arguments)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)


        viewModel.loadProfilePhoto(
            requireContext(),
            binding.profileCard.userPhoto
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

        binding.changeTheme.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_changeDiaryStyleFragment)
        }


        binding.aboutApp.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_aboutAppFragment)
        }

        binding.logoutButton.setOnClickListener { viewModel.logout(requireContext()) }

        observeMySettings()
//        observeGradesNotifications()
        observeErrors()
        observeLoading()
    }

//    private fun observeGradesNotifications() {
//        binding.newGradeNotification.root.setOnClickListener {
//            CoroutineScope(Dispatchers.IO).launch {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && viewModel.enableGradeNotifications.value == false) {
//                    checkNotificationsPermission.launch(android.Manifest.permission.POST_NOTIFICATIONS)
//                } else {
//                    viewModel.changeGradeNotifications(requireContext())
//                }
//            }
//        }
//
//        viewModel.enableGradeNotifications.observe(viewLifecycleOwner) {
//            binding.newGradeNotification.newGradeNotificationSwitch.isChecked = it
//        }
//
//        viewModel.gradesNotificationsLoading.observe(viewLifecycleOwner) {
//            binding.newGradeNotification.newGradeNotificationSwitch.isEnabled = !it
//            binding.newGradeNotification.root.isClickable = !it
//        }
//    }

    private fun observeLoading() {
        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.phoneShimmer.startShimmer()
                binding.emailShimmer.startShimmer()
                binding.profileCardShimmer.root.visibility = View.VISIBLE
                binding.profileCard.root.visibility = View.INVISIBLE

            } else {
                binding.phoneShimmer.hideShimmer()
                binding.emailShimmer.hideShimmer()
                binding.profileCard.root.visibility = View.VISIBLE
                binding.profileCardShimmer.root.visibility = View.GONE
            }
        }
    }

    private fun observeErrors() {
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun observeMySettings() {
        val regex = """(\d)(\d{3})(\d{3})(\d{2})(\d{2})""".toRegex()
        viewModel.mySettingsResponseEntity.observe(viewLifecycleOwner) {
            binding.profileCard.userName.text = requireContext().getString(
                R.string.user_name,
                it.lastName,
                it.firstName,
                it.middleName
            )
            binding.profileCard.userLogin.text = it.loginName

            binding.phoneNumberValue.text = if (!it.mobilePhone.isNullOrEmpty())
                regex.replace(it.mobilePhone, "+$1 ($2) $3-$4$5")
            else getString(R.string.empty)

            binding.emailValue.text = if (!it.email.isNullOrEmpty()) it.email
            else getString(R.string.empty)
        }
    }
}