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
import android.transition.TransitionManager
import android.view.MenuItem
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.doOnPreDraw
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialFadeThrough
import com.google.android.material.transition.platform.MaterialSharedAxis
// import com.google.firebase.analytics.FirebaseAnalytics
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.BuildConfig
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ContainerMainBinding
import com.mezhendosina.sgo.app.databinding.FragmentGradesBinding
import com.mezhendosina.sgo.app.model.grades.GradeSortType
import com.mezhendosina.sgo.app.ui.gradesFlow.filter.FilterBottomSheet
import com.mezhendosina.sgo.app.ui.gradesFlow.filter.GradesFilterViewModel
import com.mezhendosina.sgo.app.ui.gradesFlow.grades.GradesViewModel
import com.mezhendosina.sgo.app.ui.gradesFlow.grades.OnGradeClickListener
import com.mezhendosina.sgo.app.ui.journalFlow.journal.JournalPagerAdapter
import com.mezhendosina.sgo.app.ui.main.updateBottomSheet.UpdateBottomSheetFragment
import com.mezhendosina.sgo.app.uiEntities.FilterUiEntity
import com.mezhendosina.sgo.app.utils.LoadStates
import com.mezhendosina.sgo.app.utils.findTopNavController
import com.mezhendosina.sgo.app.utils.slideDownAnimation
import com.mezhendosina.sgo.app.utils.slideUpAnimation
import com.mezhendosina.sgo.data.SettingsDataStore
import com.mezhendosina.sgo.data.currentWeekStart
import com.mezhendosina.sgo.data.netschool.api.grades.entities.GradesItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class ContainerFragment :
    Fragment(R.layout.container_main),
    GradesFilterInterface,
    GradesActionsInterface,
    ContainerNavigationInterface {
    @Inject
    lateinit var settingsDataStore: SettingsDataStore

    private var binding: ContainerMainBinding? = null

    private val file: File = File.createTempFile("app", "apk")

    private val containerViewModel: ContainerViewModel by viewModels()
    private val gradesFilterViewModel: GradesFilterViewModel by viewModels()
    internal val gradesViewModel: GradesViewModel by viewModels()

    private val journalOnPageChangeCallback =
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Singleton.currentWeek = position
            }
        }

    private val weekNow = currentWeekStart()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        sharedElementEnterTransition = MaterialContainerTransform()
        sharedElementReturnTransition = MaterialContainerTransform()
        setupGrades()
        CoroutineScope(Dispatchers.IO).launch {
            containerViewModel.checkUpdates()
            containerViewModel.loadWeeks()
            containerViewModel.showUpdateDialog()
        }
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        binding = ContainerMainBinding.bind(view)
        val journalPagerAdapter =
            JournalPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        with(binding!!) {
            gradesTopBar.root.background = mainToolbar.background
            mainToolbar.setOnMenuItemClickListener { setupOnMenuItemClickListener(it) }
            bottomNavigation.setOnItemSelectedListener { onBottomNavItemClickListener(it) }

            with(grades) {
                gradesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                gradesRecyclerView.adapter = gradesViewModel.gradeAdapter

                errorMessage.retryButton.setOnClickListener {
                    Singleton.updateGradeState.value = LoadStates.UPDATE
                }
            }

            journal.adapter = journalPagerAdapter
            journal.registerOnPageChangeCallback(journalOnPageChangeCallback)
        }

        observeDownloadState()
        observeUpdates()

        view.doOnPreDraw {
            startPostponedEnterTransition()
            Singleton.gradesRecyclerViewLoaded.value = true
        }
        observeUserId()
        observeWeeks(journalPagerAdapter)

        observeGradesSort()
        observeGradesTrim()
        observeGradesYear()

        onGradesSortClickListener()
        onGradesTrimClickListener()
        onGradesYearClickListener()

        observeGradesLoadState()
        observeGrades()

        observeContainerScreen()
    }

    override fun onDestroy() {
        binding?.journal?.unregisterOnPageChangeCallback(journalOnPageChangeCallback)
        binding = null
        super.onDestroy()
        file.delete()
    }

    override fun observeUserId() {
        settingsDataStore.getValue(SettingsDataStore.CURRENT_USER_ID).asLiveData()
            .observe(viewLifecycleOwner) {
                if (it != null) {
                    binding?.journal?.invalidate()
                }
            }
    }

    private fun observeWeeks(journalPagerAdapter: JournalPagerAdapter) {
        containerViewModel.weeks.observe(viewLifecycleOwner) { entityList ->
            binding?.let { containerMainBinding ->
                val tabLayoutMediator =
                    TabLayoutMediator(
                        containerMainBinding.tabsLayout,
                        containerMainBinding.journal,
                    ) { tab, position ->
                        val item = entityList[position]
                        tab.text =
                            "${item.formattedWeekStart} - ${item.formattedWeekEnd}"
                    }
                tabLayoutMediator.attach()
                journalPagerAdapter.weeksList = entityList
                if (Singleton.currentWeek == null) {
                    val currentWeekIndex =
                        entityList.indexOf(entityList.firstOrNull { it.weekStart == weekNow })
                    containerMainBinding.journal.setCurrentItem(
                        if (currentWeekIndex != -1) currentWeekIndex else entityList.lastIndex,
                        false,
                    )
                } else {
                    containerMainBinding.journal.setCurrentItem(Singleton.currentWeek!!, false)
                }
            }
        }
    }

    override fun onBottomNavItemClickListener(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.journalFragment -> {
                Singleton.mainContainerScreen.value = JOURNAL
                true
            }

            R.id.gradesFragment -> {
                Singleton.mainContainerScreen.value = GRADES
                true
            }

            else -> false
        }
    }

    override fun observeContainerScreen() {
        Singleton.mainContainerScreen.observe(viewLifecycleOwner) { mainContainerScreen ->
            binding?.let {
                when (mainContainerScreen) {
                    JOURNAL -> {
                        it.mainToolbar.setTitle(R.string.che_zadali)
                        it.slideDownAnimation()
                        it.grades.root.visibility = View.GONE
                        it.journal.visibility = View.VISIBLE
                    }

                    GRADES -> {
                        it.mainToolbar.setTitle(R.string.grades)
                        Singleton.updateGradeState.value = LoadStates.UPDATE
                        it.slideUpAnimation()
                        it.journal.visibility = View.GONE
                        it.grades.root.visibility = View.VISIBLE
                        CoroutineScope(Dispatchers.IO).launch {
                            gradesFilterViewModel.getYearsList()
                            gradesFilterViewModel.getGradeSort()
                        }
                    }
                }
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
                    file,
                ).show(childFragmentManager, UpdateBottomSheetFragment.TAG)
                true
            }

            R.id.settings -> {
                findTopNavController().navigate(
                    R.id.action_containerFragment_to_settingsContainer,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.containerFragment, false).build(),
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
                val modalSheet =
                    UpdateBottomSheetFragment.newInstance(
                        requireContext(),
                        updates,
                        containerViewModel,
                        file,
                    )
                modalSheet.show(childFragmentManager, UpdateBottomSheetFragment.TAG)
            }
            if (updates.tagName != BuildConfig.VERSION_NAME) {
                binding?.mainToolbar?.menu?.get(0)?.isVisible =
                    true
            }
        }
    }

    private fun observeDownloadState() {
        containerViewModel.downloadState.observe(viewLifecycleOwner) { downloadState ->
            binding?.let {
                val updateProgress = it.updateProgress.root

                when (downloadState) {
                    0, 100 -> updateProgress.visibility = View.GONE
                    else -> {
                        updateProgress.visibility = View.VISIBLE
                        it.updateProgress.updateProgress.progress = downloadState
                    }
                }
            }
        }
    }

    override fun observeGradesTrim() {
        Singleton.gradesTerms.observe(viewLifecycleOwner) { gradeOptions ->
            binding?.let { containerMainBinding ->
                if (gradeOptions != null) {
                    containerMainBinding.gradesTopBar.term.visibility = View.VISIBLE
                    CoroutineScope(Dispatchers.Main).launch {
                        val trimId =
                            settingsDataStore.getValue(SettingsDataStore.TRIM_ID).first()
                        containerMainBinding.gradesTopBar.term.text =
                            gradeOptions.firstOrNull { it.id == trimId }?.name
                    }
                } else {
                    containerMainBinding.gradesTopBar.term.visibility = View.GONE
                }
            }
        }
    }

    override fun onGradesTrimClickListener() {
        binding?.gradesTopBar?.term?.setOnClickListener {
            if (Singleton.gradesTerms.value != null) {
                val filterBottomSheet =
                    FilterBottomSheet(
                        requireContext().getString(R.string.selected_grade_period),
                        Singleton.gradesTerms.value!!,
                    ) {
                        gradesFilterViewModel.changeTrimId(it)
                    }

                filterBottomSheet.show(
                    requireActivity().supportFragmentManager,
                    "trim_selector_bottom_sheet",
                )
            }
        }
    }

    override fun observeGradesYear() {
        gradesFilterViewModel.yearList.observe(viewLifecycleOwner) { yearList ->

            binding?.let { containerMainBinding ->
                val checkedItem = yearList.find { it.checked }
                if (!yearList.isNullOrEmpty() && checkedItem != null) {
                    containerMainBinding.gradesTopBar.year.visibility = View.VISIBLE
                    containerMainBinding.gradesTopBar.year.isChecked =
                        checkedItem.id != gradesFilterViewModel.currentYearId.value
                    containerMainBinding.gradesTopBar.year.text = checkedItem.name
                } else {
                    containerMainBinding.gradesTopBar.year.visibility = View.GONE
                }
            }
        }
    }

    override fun onGradesYearClickListener() {
        binding?.gradesTopBar?.year?.setOnClickListener {
            val items = gradesFilterViewModel.yearList.value!!
            val filterBottomSheet =
                FilterBottomSheet(
                    requireContext().getString(R.string.year_grade_header),
                    items,
                ) {
                    CoroutineScope(Dispatchers.IO).launch {
                        gradesFilterViewModel.updateYear(it)
                    }
                }
            filterBottomSheet.show(
                requireActivity().supportFragmentManager,
                "filter_grade_year",
            )
        }
    }

    override fun observeGradesSort() {
        gradesFilterViewModel.gradesSortType.observe(viewLifecycleOwner) {
            binding?.gradesTopBar?.sortGrades?.text = GradeSortType.toString(requireContext(), it)
        }
    }

    override fun onGradesSortClickListener() {
        binding?.let {
            it.gradesTopBar.sortGrades.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    val selectedItem =
                        settingsDataStore.getValue(
                            SettingsDataStore.SORT_GRADES_BY,
                        ).first()
                    val list =
                        listOf(
                            FilterUiEntity(
                                GradeSortType.BY_GRADE_VALUE,
                                GradeSortType.toString(
                                    requireContext(),
                                    GradeSortType.BY_GRADE_VALUE,
                                ),
                                selectedItem == GradeSortType.BY_GRADE_VALUE,
                            ),
                            FilterUiEntity(
                                GradeSortType.BY_GRADE_VALUE_DESC,
                                GradeSortType.toString(
                                    requireContext(),
                                    GradeSortType.BY_GRADE_VALUE_DESC,
                                ),
                                selectedItem == GradeSortType.BY_GRADE_VALUE_DESC,
                            ),
                            FilterUiEntity(
                                GradeSortType.BY_LESSON_NAME,
                                GradeSortType.toString(
                                    requireContext(),
                                    GradeSortType.BY_LESSON_NAME,
                                ),
                                selectedItem == GradeSortType.BY_LESSON_NAME,
                            ),
                        )
                    val bottomSheet =
                        FilterBottomSheet(
                            requireContext().getString(R.string.sort_grades_by),
                            list,
                        ) { id ->
                            gradesFilterViewModel.setGradeSort(id)
                        }
                    bottomSheet.show(
                        requireActivity().supportFragmentManager,
                        "sort_grade_bottom_sheet",
                    )
                }
            }
        }
    }

    override fun setupGrades() {
        if (gradesViewModel.gradeAdapter == null) {
            gradesViewModel.setAdapter(
                object : OnGradeClickListener {
                    override fun invoke(
                        p1: GradesItem,
                        p2: View,
                    ) {
                        val navigationExtras =
                            FragmentNavigatorExtras(
                                p2 to getString(R.string.grade_item_details_transition_name),
                            )
                        gradesViewModel.setLesson(p1)

                        findTopNavController().navigate(
                            R.id.action_containerFragment_to_gradeItemFragment,
                            null,
                            null,
                            navigationExtras,
                        )
                        Singleton.gradesRecyclerViewLoaded.value = false
                    }
                },
            )
        }
    }

    override fun observeGrades() {
        gradesViewModel.grades.observe(viewLifecycleOwner) { list ->
            gradesViewModel.gradeAdapter?.grades = list
        }
    }

    override fun observeGradesError() {
        gradesViewModel.errorMessage.observe(viewLifecycleOwner) {
            binding?.grades?.errorMessage?.errorDescription?.text = it
        }
    }

    override fun observeGradesLoadState() {
        val fadeThrough = MaterialFadeThrough()

        Singleton.updateGradeState.observe(viewLifecycleOwner) { loadState ->
            binding?.let {
                with(it.grades) {
                    when (loadState) {
                        LoadStates.UPDATE -> {
                            CoroutineScope(Dispatchers.IO).launch {
                                gradesViewModel.load()
                            }
                            TransitionManager.beginDelayedTransition(
                                loading.root,
                                fadeThrough,
                            )
                            TransitionManager.beginDelayedTransition(
                                gradesRecyclerView,
                                fadeThrough,
                            )

                            loading.root.startShimmer()
                            showLoading()
                        }

                        LoadStates.ERROR -> {
                            loading.root.stopShimmer()
                            showError()
                        }

                        LoadStates.FINISHED -> {
                            loading.root.stopShimmer()
                            if (gradesViewModel.grades.value.isNullOrEmpty()) {
                                emptyState.noHomeworkIcon.setImageDrawable(
                                    AppCompatResources.getDrawable(
                                        requireContext(),
                                        R.drawable.ic_emty_grade,
                                    ),
                                )
                                emptyState.emptyText.text = "Оценок нет"
                                showEmptyState()
                            } else {
                                gradesRecyclerView.doOnPreDraw {
                                    showGrades()
                                }
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    override fun FragmentGradesBinding.showGrades() {
        gradesRecyclerView.visibility = View.VISIBLE
        emptyState.root.visibility = View.GONE
        loading.root.visibility = View.GONE
        errorMessage.root.visibility = View.GONE
    }

    override fun FragmentGradesBinding.showEmptyState() {
        emptyState.root.visibility = View.VISIBLE
        gradesRecyclerView.visibility = View.INVISIBLE
        loading.root.visibility = View.INVISIBLE
        errorMessage.root.visibility = View.INVISIBLE
    }

    override fun FragmentGradesBinding.showLoading() {
        loading.root.visibility = View.VISIBLE
        gradesRecyclerView.visibility = View.INVISIBLE
        emptyState.root.visibility = View.GONE
        errorMessage.root.visibility = View.GONE
    }

    override fun FragmentGradesBinding.showError() {
        errorMessage.root.visibility = View.VISIBLE
        gradesRecyclerView.visibility = View.INVISIBLE
        emptyState.root.visibility = View.GONE
        loading.root.visibility = View.GONE
    }

    companion object {
        const val JOURNAL = "journal"
        const val GRADES = "grades"
    }
}
