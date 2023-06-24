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

package com.mezhendosina.sgo.app.ui.main.container

import android.content.Context
import android.os.Bundle
import android.transition.TransitionManager
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import androidx.core.view.doOnPreDraw
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
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.google.firebase.analytics.FirebaseAnalytics
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.BuildConfig
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ContainerMainBinding
import com.mezhendosina.sgo.app.model.journal.DiaryStyle
import com.mezhendosina.sgo.app.ui.announcementsFlow.announcementsBottomSheet.AnnouncementsBottomSheet
import com.mezhendosina.sgo.app.ui.gradesFlow.gradesFilter.GradesFilterBottomSheet
import com.mezhendosina.sgo.app.ui.gradesFlow.gradesFilter.GradesFilterViewModel
import com.mezhendosina.sgo.app.ui.main.updateBottomSheet.UpdateBottomSheetFragment
import com.mezhendosina.sgo.app.utils.findTopNavController
import com.mezhendosina.sgo.data.SettingsDataStore
import com.mezhendosina.sgo.data.editPreference
import com.mezhendosina.sgo.data.getValue
import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton
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
//        sharedElementEnterTransition = MaterialContainerTransform()
//        sharedElementReturnTransition = MaterialContainerTransform()

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.checkUpdates()
            viewModel.showUpdateDialog(requireContext())

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        binding = ContainerMainBinding.bind(view)
        Singleton.journalTabsLayout = binding.tabsLayout
        val navHost = childFragmentManager.findFragmentById(R.id.tabs_container) as NavHostFragment
        val navController = navHost.navController
        navController.addOnDestinationChangedListener(onDestinationChangedListener)

        binding.bottomNavigation.setupWithNavController(navController)
        CoroutineScope(Dispatchers.Main).launch {
            NavigationUI.setupWithNavController(
                binding.toolbar,
                navController,
                AppBarConfiguration(setOf(R.id.gradesFragment, R.id.journalFragment))
            )
        }
        binding.toolbar.setOnMenuItemClickListener { setupOnMenuItemClickListener(it) }

        binding.gradesTopBar.termSelector.setOnClickListener(onTermSelectedListener())

        observeDownloadState()
        observeUpdates()

        view.doOnPreDraw {
            startPostponedEnterTransition()
            Singleton.gradesRecyclerViewLoaded.value = true
        }
        observeGradesOptions(requireContext())
//        observeGradesRecyclerViewLoad()
        setupOnSortGradesClickListener()
        observeDiaryStyle()
        observeGradeYear()
    }

    override fun onDestroy() {
        super.onDestroy()
        file.delete()
        Singleton.journalTabsLayout = null
        Singleton.tabLayoutMediator = null
        findNavController().removeOnDestinationChangedListener(onDestinationChangedListener)
    }

//    private fun observeGradesRecyclerViewLoad() {
//        Singleton.gradesRecyclerViewLoaded.observe(viewLifecycleOwner) {
//            if (!it) {
//                startPostponedEnterTransition()
//                Singleton.gradesRecyclerViewLoaded.value = true
//            }
//        }
//    }

    private fun observeGradeYear() {
        NetSchoolSingleton.gradesYearId.observe(viewLifecycleOwner) {
            binding.toolbar.title = if (it.id != NetSchoolSingleton.journalYearId.value) {
                requireContext().getString(
                    R.string.grades_title_with_year,
                    GradesFilterViewModel.filterYearName(it.name)
                )
            } else {
                requireContext().getString(R.string.grades)
            }
        }
    }

    private fun observeGradesOptions(context: Context) {
        Singleton.gradesOptions.observe(viewLifecycleOwner) { gradeOptions ->
            if (gradeOptions != null) {
                CoroutineScope(Dispatchers.Main).launch {
                    val trimId =
                        SettingsDataStore.CURRENT_TRIM_ID.getValue(requireContext(), "").first()
                    binding.gradesTopBar.termSelector.text =
                        gradeOptions.TERMID.firstOrNull { it.value == trimId }?.name
                }
            }
        }
    }

    private fun observeDiaryStyle() {
        val firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
        CoroutineScope(Dispatchers.Main).launch {
            SettingsDataStore.DIARY_STYLE.getValue(requireContext(), DiaryStyle.AS_CARD).collect {
                firebaseAnalytics.setUserProperty("diary_style", it)
            }
        }
    }

    private fun setupOnSortGradesClickListener() {
        binding.gradesTopBar.sortGradesButton.setOnClickListener {
            GradesFilterBottomSheet().show(
                childFragmentManager,
                GradesFilterBottomSheet.TAG
            )
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

            CoroutineScope(Dispatchers.IO).launch {
                Singleton.gradesOptions.value?.TERMID?.get(position)?.let {
                    SettingsDataStore.CURRENT_TRIM_ID.editPreference(requireContext(), it.value)
                }
            }
        }
        popup.show()
    }

    private fun setupOnMenuItemClickListener(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.update_available -> {
                UpdateBottomSheetFragment.newInstance(
                    requireContext(), viewModel.latestUpdate.value!!, viewModel, file
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
                val modalSheet = UpdateBottomSheetFragment.newInstance(
                    requireContext(),
                    updates,
                    viewModel,
                    file
                )
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
