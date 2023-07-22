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

import android.os.Bundle
import android.view.MenuItem
import android.view.View
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.google.firebase.analytics.FirebaseAnalytics
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.BuildConfig
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ContainerMainBinding
import com.mezhendosina.sgo.app.model.grades.GradeSortType
import com.mezhendosina.sgo.app.model.journal.DiaryStyle
import com.mezhendosina.sgo.app.ui.gradesFlow.gradesFilter.GradesFilterViewModel
import com.mezhendosina.sgo.app.ui.main.updateBottomSheet.UpdateBottomSheetFragment
import com.mezhendosina.sgo.app.utils.findTopNavController
import com.mezhendosina.sgo.app.utils.slideDownAnimation
import com.mezhendosina.sgo.app.utils.slideUpAnimation
import com.mezhendosina.sgo.data.SettingsDataStore
import com.mezhendosina.sgo.data.getValue
import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File

class ContainerFragment : Fragment(R.layout.container_main), GradesFilterInterface {

    private lateinit var binding: ContainerMainBinding

    private val file: File = File.createTempFile("app", "apk")

    private val containerViewModel: ContainerViewModel by viewModels()

    private val gradesFilterViewModel: GradesFilterViewModel by viewModels()

    private val onDestinationChangedListener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.journalFragment -> {
                    binding.slideDownAnimation()
                }

                R.id.gradesFragment -> {
                    binding.slideUpAnimation()
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
            containerViewModel.checkUpdates()
            containerViewModel.showUpdateDialog(requireContext())

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

        observeDownloadState()
        observeUpdates()

        view.doOnPreDraw {
            startPostponedEnterTransition()
            Singleton.gradesRecyclerViewLoaded.value = true
        }
        observeDiaryStyle()

        observeGradesSort()
        observeGradesTrim()
        observeGradesYear()
        onGradesSortClickListener()
        onGradesTrimClickListener()
        onGradesYearClickListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        file.delete()
        Singleton.journalTabsLayout = null
        Singleton.tabLayoutMediator = null
        findNavController().removeOnDestinationChangedListener(onDestinationChangedListener)
    }

    private fun observeGradesRecyclerViewLoad() {
        Singleton.gradesRecyclerViewLoaded.observe(viewLifecycleOwner) {
            if (!it) {
                startPostponedEnterTransition()
                Singleton.gradesRecyclerViewLoaded.value = true
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

    private fun setupOnMenuItemClickListener(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.update_available -> {
                UpdateBottomSheetFragment.newInstance(
                    requireContext(),
                    containerViewModel.latestUpdate.value!!,
                    containerViewModel,
                    file
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
                findTopNavController().navigate(R.id.action_containerFragment_to_announcementsContainerFragment2)
                true
            }

            else -> false
        }

    }

    private fun observeUpdates() {
        containerViewModel.latestUpdate.observe(viewLifecycleOwner) { updates ->
            if (updates.tagName != BuildConfig.VERSION_NAME && containerViewModel.showUpdateDialog.value != false) {
                val modalSheet = UpdateBottomSheetFragment.newInstance(
                    requireContext(),
                    updates,
                    containerViewModel,
                    file
                )
                modalSheet.show(childFragmentManager, UpdateBottomSheetFragment.TAG)
            }
            if (updates.tagName != BuildConfig.VERSION_NAME) binding.toolbar.menu[0].isVisible =
                true
        }
    }

    private fun observeDownloadState() {
        containerViewModel.downloadState.observe(viewLifecycleOwner) {
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

    override fun observeGradesTrim() {
        Singleton.gradesTerms.observe(viewLifecycleOwner) { gradeOptions ->
            if (gradeOptions != null) {
                binding.gradesTopBar.term.visibility = View.VISIBLE
                CoroutineScope(Dispatchers.Main).launch {
                    val trimId =
                        SettingsDataStore.CURRENT_TRIM_ID.getValue(requireContext(), -1).first()
                    binding.gradesTopBar.term.text =
                        gradeOptions.firstOrNull { it.id == trimId }?.name
                }
            } else {
                binding.gradesTopBar.term.visibility = View.GONE
            }
        }
    }

    override fun onGradesTrimClickListener() {
        binding.gradesTopBar.term.setOnClickListener {
            val trims = Singleton.gradesTerms.value!!
            val trimsNames = trims.map { it.name }.toTypedArray()
            val selected = trims.first { it.selected }
            val selectedTrimsId = trimsNames.indexOfFirst { selected.name == it }
            var newSelectedItem = selectedTrimsId
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.selected_grade_period))
                .setSingleChoiceItems(trimsNames, selectedTrimsId) { _, item ->
                    newSelectedItem = item
                }
                .setPositiveButton(R.string.set) { dialog, _ ->
                    val newSelectedTrimId =
                        trims.first { it.name == trimsNames[newSelectedItem] }.id
                    gradesFilterViewModel.changeTrimId(requireContext(), newSelectedTrimId)
                    dialog.dismiss()
                }
        }
    }

    override fun observeGradesYear() {
        NetSchoolSingleton.gradesYearId.observe(viewLifecycleOwner) {
            binding.gradesTopBar.year.isChecked = it != gradesFilterViewModel.currentYear.value
            binding.gradesTopBar.year.text = GradesFilterViewModel.filterYearName(it.name)
        }
    }

    override fun onGradesYearClickListener() {
        binding.gradesTopBar.year.setOnClickListener {
            // мне стыдно за следующие 17 строк
            val items =
                gradesFilterViewModel.yearList.value!!.map { GradesFilterViewModel.filterYearName(it.name) }
                    .toTypedArray()
            val selectedYearId = gradesFilterViewModel.currentYear.value
            val selectedYearName = GradesFilterViewModel.filterYearName(selectedYearId!!.name)
            val selectedYearArrayIndex = items.indexOf(selectedYearName)
            var newYearId = selectedYearArrayIndex
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.year_grade_header)
                .setSingleChoiceItems(items, selectedYearArrayIndex) { _, which ->
                    newYearId = which
                }
                .setPositiveButton("Ок") { dialog, _ ->
                    val selectedId =
                        gradesFilterViewModel.yearList.value?.first { it.name.contains(items[newYearId]) }
                    gradesFilterViewModel.changeSelectedYear(selectedId!!)
                    CoroutineScope(Dispatchers.IO).launch {
                        gradesFilterViewModel.updateYear()
                    }
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    override fun observeGradesSort() {
        gradesFilterViewModel.gradesSortType.observe(viewLifecycleOwner) {
            binding.gradesTopBar.sortGrades.text = GradeSortType.toString(requireContext(), it)
        }
    }

    override fun onGradesSortClickListener() {
        binding.gradesTopBar.sortGrades.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                var selectedItem = SettingsDataStore.SORT_GRADES_BY.getValue(
                    requireContext(),
                    GradeSortType.BY_LESSON_NAME
                ).first()
                val list = arrayListOf(
                    GradeSortType.toString(requireContext(), GradeSortType.BY_GRADE_VALUE),
                    GradeSortType.toString(requireContext(), GradeSortType.BY_GRADE_VALUE_DESC),
                    GradeSortType.toString(requireContext(), GradeSortType.BY_LESSON_NAME),
                ).toTypedArray()
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.sort_grades_by)
                    .setSingleChoiceItems(list, selectedItem) { _, item ->
                        selectedItem = item
                    }
                    .setPositiveButton(R.string.set) { dialog, _ ->
                        gradesFilterViewModel.setGradeSort(requireContext(), selectedItem)
                        dialog.dismiss()
                    }
            }
        }
    }

}
