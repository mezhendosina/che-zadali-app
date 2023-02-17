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

package com.mezhendosina.sgo.app.ui.container

import android.os.Bundle
import android.transition.TransitionManager
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.BuildConfig
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ContainerMainBinding
import com.mezhendosina.sgo.app.findTopNavController
import com.mezhendosina.sgo.app.ui.TOOLBAR
import com.mezhendosina.sgo.app.ui.announcementsBottomSheet.AnnouncementsBottomSheet
import com.mezhendosina.sgo.app.ui.isB
import com.mezhendosina.sgo.app.ui.updateBottomSheet.UpdateBottomSheetFragment
import com.mezhendosina.sgo.data.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File

class ContainerFragment : Fragment(R.layout.container_main) {

    private lateinit var binding: ContainerMainBinding

    private val file: File = File.createTempFile("app", "apk")

    private val viewModel: ContainerViewModel by viewModels()

    private val onDestinationChangedListener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.journalFragment -> {
                    slideDownAnimation()
                }
                R.id.gradesFragment -> {
                    slideUpAnimation()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        sharedElementEnterTransition = MaterialContainerTransform()
        sharedElementReturnTransition = MaterialContainerTransform()
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.checkUpdates()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ContainerMainBinding.bind(view)
        Singleton.journalTabsLayout = binding.tabsLayout
        val navHost = childFragmentManager.findFragmentById(R.id.tabs_container) as NavHostFragment
        val navController = navHost.navController
        navController.addOnDestinationChangedListener(onDestinationChangedListener)

        binding.bottomNavigation.setupWithNavController(navController)
        CoroutineScope(Dispatchers.Main).launch {
            if (isB()) {
                binding.toolbar.setTitleTextAppearance(
                    requireContext(),
                    R.style.TextAppearance_SGOApp_ActionBar_Title
                )
                binding.toolbar.title = TOOLBAR
            } else {
                NavigationUI.setupWithNavController(
                    binding.toolbar,
                    navController,
                    AppBarConfiguration(setOf(R.id.gradesFragment, R.id.journalFragment))
                )
            }
        }
        binding.toolbar.setOnMenuItemClickListener { setupOnMenuItemClickListener(it) }
        binding.gradesTopBar.termSelector.setOnClickListener(onTermSelectedListener())
        observeDownloadState()
        observeUpdates()
        observeGradesOptions()
    }

    override fun onDestroy() {
        super.onDestroy()
        file.delete()
        Singleton.journalTabsLayout = null
        Singleton.tabLayoutMediator = null
        findNavController().removeOnDestinationChangedListener(onDestinationChangedListener)
    }

    private fun observeGradesOptions() {
        val settings = Settings(Singleton.getContext())
        Singleton.gradesOptions.observe(viewLifecycleOwner) { gradeOptions ->
            if (gradeOptions != null) {
                CoroutineScope(Dispatchers.Main).launch {
                    binding.gradesTopBar.termSelector.text =
                        gradeOptions.TERMID.first { it.value == settings.currentTrimId.first() }.name
                }
            }
        }
    }

    private fun onTermSelectedListener(): View.OnClickListener = View.OnClickListener { it1 ->
        val popup = ListPopupWindow(
            requireContext(),
            null,
            com.google.android.material.R.attr.listPopupWindowStyle
        )

        popup.anchorView = it1

        val adapter =
            ArrayAdapter(
                requireContext(),
                R.layout.item_list_popup_window,
                Singleton.gradesOptions.value?.TERMID?.map { it.name } ?: emptyList()
            )
        popup.setAdapter(adapter)

        popup.setOnItemClickListener { _, _, position, _ ->
            popup.dismiss()
            val settings = Settings(requireContext())

            CoroutineScope(Dispatchers.IO).launch {
                Singleton.gradesOptions.value?.TERMID?.get(position)?.let {
                    settings.editPreference(Settings.CURRENT_TRIM_ID, it.value)
                }
            }
        }
        popup.show()
    }

    private fun setupOnMenuItemClickListener(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.update_available -> {
                UpdateBottomSheetFragment.newInstance(
                    viewModel.latestUpdate.value!!, viewModel, file
                ).show(childFragmentManager, UpdateBottomSheetFragment.TAG)
                true
            }
            R.id.settings -> {
                findTopNavController().navigate(
                    R.id.action_containerFragment_to_settingsContainer,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.containerFragment, false).build()
                )
                true
            }
            R.id.announcements -> {
                AnnouncementsBottomSheet().show(
                    childFragmentManager, AnnouncementsBottomSheet.TAG
                )
                true
            }
            else -> false
        }

    }


    private fun observeUpdates() {
        viewModel.latestUpdate.observe(viewLifecycleOwner) { updates ->
            if (updates.tagName != BuildConfig.VERSION_NAME && viewModel.showUpdateDialog.value != false) {
                val modalSheet = UpdateBottomSheetFragment.newInstance(updates, viewModel, file)
                modalSheet.show(childFragmentManager, UpdateBottomSheetFragment.TAG)
            }
            if (updates.tagName != BuildConfig.VERSION_NAME) binding.toolbar.menu[0].isVisible =
                true

        }
    }

    private fun observeDownloadState() {
        viewModel.downloadState.observe(viewLifecycleOwner) {
            val updateProgress = binding.updateProgress.root

            when (it) {
                0, 100 -> updateProgress.visibility = View.GONE
                else -> {
                    updateProgress.visibility = View.VISIBLE
                    binding.updateProgress.updateProgress.progress = it
                }
            }
        }
    }


    private fun slideDownAnimation() {
        this.apply {
            val materialFade = MaterialSharedAxis(MaterialSharedAxis.Y, false)
            TransitionManager.beginDelayedTransition(binding.appbarLayout, materialFade)
            binding.tabsLayout.visibility = View.VISIBLE
            binding.gradesTopBar.root.visibility = View.GONE

        }
    }

    private fun slideUpAnimation() {
        val materialFade = MaterialSharedAxis(MaterialSharedAxis.Y, true)
        TransitionManager.beginDelayedTransition(binding.appbarLayout, materialFade)
        binding.tabsLayout.visibility = View.GONE
        binding.gradesTopBar.root.visibility = View.VISIBLE

    }
}
