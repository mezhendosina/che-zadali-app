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

import android.content.Intent
import android.os.Bundle
import android.transition.TransitionManager
import android.view.MenuItem
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.platform.MaterialFadeThrough
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.google.firebase.analytics.FirebaseAnalytics
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.BuildConfig
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ContainerMainBinding
import com.mezhendosina.sgo.app.databinding.FragmentGradesBinding
import com.mezhendosina.sgo.app.model.grades.GradeSortType
import com.mezhendosina.sgo.app.model.journal.DiaryStyle
import com.mezhendosina.sgo.app.ui.gradesFlow.filter.FilterBottomSheet
import com.mezhendosina.sgo.app.ui.gradesFlow.filter.GradesFilterViewModel
import com.mezhendosina.sgo.app.ui.gradesFlow.grades.GradeAdapter
import com.mezhendosina.sgo.app.ui.gradesFlow.grades.GradesViewModel
import com.mezhendosina.sgo.app.ui.gradesFlow.grades.OnGradeClickListener
import com.mezhendosina.sgo.app.ui.journalFlow.journal.JournalPagerAdapter
import com.mezhendosina.sgo.app.ui.main.updateBottomSheet.UpdateBottomSheetFragment
import com.mezhendosina.sgo.app.uiEntities.FilterUiEntity
import com.mezhendosina.sgo.app.utils.GradeUpdateStatus
import com.mezhendosina.sgo.app.utils.findTopNavController
import com.mezhendosina.sgo.app.utils.slideDownAnimation
import com.mezhendosina.sgo.app.utils.slideUpAnimation
import com.mezhendosina.sgo.data.SettingsDataStore
import com.mezhendosina.sgo.data.currentWeekStart
import com.mezhendosina.sgo.data.getValue
import com.mezhendosina.sgo.data.netschool.api.grades.entities.GradesItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File

class ContainerFragment
    : Fragment(R.layout.container_main), GradesFilterInterface, GradesActionsInterface,
    ContainerNavigationInterface {

    private lateinit var binding: ContainerMainBinding

    private val file: File = File.createTempFile("app", "apk")

    private val containerViewModel: ContainerViewModel by viewModels()
    private val gradesFilterViewModel: GradesFilterViewModel by viewModels()
    internal val gradesViewModel: GradesViewModel by viewModels()

    private val gradeAdapter = GradeAdapter(object : OnGradeClickListener {
        override fun invoke(p1: GradesItem, p2: View) {
            val a = gradesViewModel.grades.value?.indexOf(p1)

            val navigationExtras = FragmentNavigatorExtras(
                p2 to getString(R.string.grade_item_details_transition_name)
            )

            findTopNavController().navigate(
                R.id.action_containerFragment_to_gradeItemFragment,
                bundleOf("LESSON_INDEX" to a),
                null,
                navigationExtras
            )
            Singleton.gradesRecyclerViewLoaded.value = false
        }
    })

    private val journalOnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
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
//        sharedElementEnterTransition = MaterialContainerTransform()
//        sharedElementReturnTransition = MaterialContainerTransform()

        CoroutineScope(Dispatchers.IO).launch {
            containerViewModel.checkUpdates()
            containerViewModel.loadWeeks()
            containerViewModel.showUpdateDialog(requireContext())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        binding = ContainerMainBinding.bind(view)
        val journalPagerAdapter =
            JournalPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)

        binding.gradesTopBar.root.background = binding.toolbar.background
        binding.toolbar.setOnMenuItemClickListener { setupOnMenuItemClickListener(it) }
        binding.bottomNavigation.setOnItemSelectedListener { onBottomNavItemClickListener(it) }

        with(binding.grades) {
            gradesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            gradesRecyclerView.adapter = gradeAdapter

            errorMessage.retryButton.setOnClickListener {
                Singleton.updateGradeState.value = GradeUpdateStatus.UPDATE
            }
        }

        observeDownloadState()
        observeUpdates()

        binding.journal.adapter = journalPagerAdapter
        binding.journal.registerOnPageChangeCallback(journalOnPageChangeCallback)

        view.doOnPreDraw {
            startPostponedEnterTransition()
            Singleton.gradesRecyclerViewLoaded.value = true
        }
        observeDiaryStyle()
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
        observeShowEngageDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        file.delete()
        binding.journal.unregisterOnPageChangeCallback(journalOnPageChangeCallback)
    }

    private fun observeShowEngageDialog() {
        containerViewModel.showEngageDialog.observe(viewLifecycleOwner) {
            if (it) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Понравилось приложение?")
                    .setMessage("Если да, то поделись им с одноклассниками, чтобы они тоже могли воспользоваться самым удобным дневником!")
                    .setPositiveButton("Поделиться") { dialog, _ ->
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "https://sgoapp.ru")
                            type = "text/plain"
                        }
                        val shareIntent =
                            Intent.createChooser(sendIntent, "Поделиться ссылкой на SGO app")
                        startActivity(shareIntent)
                        dialog.dismiss()
                    }.setNegativeButton("Нет") { dialog, _ ->
                        dialog.cancel()
                    }.show()
            }
        }
    }

    override fun observeUserId() {
        SettingsDataStore.CURRENT_USER_ID.getValue(requireContext(), -1).asLiveData()
            .observe(viewLifecycleOwner) {
                binding.journal.invalidate()
            }
    }

    private fun observeWeeks(journalPagerAdapter: JournalPagerAdapter) {
        containerViewModel.weeks.observe(viewLifecycleOwner) { entityList ->
            val tabLayoutMediator = TabLayoutMediator(
                binding.tabsLayout,
                binding.journal
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
                binding.journal.setCurrentItem(
                    if (currentWeekIndex != -1) currentWeekIndex else entityList.lastIndex,
                    false
                )
            } else {
                binding.journal.setCurrentItem(Singleton.currentWeek!!, false)
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
        Singleton.mainContainerScreen.observe(viewLifecycleOwner) {
            when (it) {
                JOURNAL -> {
                    binding.toolbar.setTitle(R.string.journal)
                    binding.slideDownAnimation()
                    binding.grades.root.visibility = View.GONE
                    binding.journal.visibility = View.VISIBLE
                }

                GRADES -> {
                    binding.toolbar.setTitle(R.string.grades)
                    Singleton.updateGradeState.value = GradeUpdateStatus.UPDATE
                    binding.slideUpAnimation()
                    binding.journal.visibility = View.GONE
                    binding.grades.root.visibility = View.VISIBLE
                    CoroutineScope(Dispatchers.IO).launch {
                        gradesFilterViewModel.getYearsList()
                        gradesFilterViewModel.getGradeSort(requireContext())
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
                        SettingsDataStore.TRIM_ID.getValue(requireContext(), -1).first()
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
            if (Singleton.gradesTerms.value != null) {
                val filterBottomSheet = FilterBottomSheet(
                    requireContext().getString(R.string.selected_grade_period),
                    Singleton.gradesTerms.value!!
                ) {
                    gradesFilterViewModel.changeTrimId(requireContext(), it)
                }

                filterBottomSheet.show(
                    requireActivity().supportFragmentManager,
                    "trim_selector_bottom_sheet"
                )
            }
        }
    }

    override fun observeGradesYear() {
        gradesFilterViewModel.yearList.observe(viewLifecycleOwner) { yearList ->
            val checkedItem = yearList.find { it.checked }
            if (!yearList.isNullOrEmpty() && checkedItem != null) {
                binding.gradesTopBar.year.visibility = View.VISIBLE
                binding.gradesTopBar.year.isChecked =
                    checkedItem.id != gradesFilterViewModel.currentYearId.value
                binding.gradesTopBar.year.text = checkedItem.name
            } else {
                binding.gradesTopBar.year.visibility = View.GONE
            }
        }
    }

    override fun onGradesYearClickListener() {
        binding.gradesTopBar.year.setOnClickListener {
            val items = gradesFilterViewModel.yearList.value!!
            val filterBottomSheet = FilterBottomSheet(
                requireContext().getString(R.string.year_grade_header),
                items
            ) {
                CoroutineScope(Dispatchers.IO).launch {
                    gradesFilterViewModel.updateYear(it)
                }
            }
            filterBottomSheet.show(
                requireActivity().supportFragmentManager,
                "filter_grade_year"
            )
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
                val selectedItem = SettingsDataStore.SORT_GRADES_BY.getValue(
                    requireContext(),
                    GradeSortType.BY_LESSON_NAME
                ).first()
                val list = listOf(
                    FilterUiEntity(
                        GradeSortType.BY_GRADE_VALUE,
                        GradeSortType.toString(requireContext(), GradeSortType.BY_GRADE_VALUE),
                        selectedItem == GradeSortType.BY_GRADE_VALUE
                    ),
                    FilterUiEntity(
                        GradeSortType.BY_GRADE_VALUE_DESC,
                        GradeSortType.toString(
                            requireContext(),
                            GradeSortType.BY_GRADE_VALUE_DESC
                        ),
                        selectedItem == GradeSortType.BY_GRADE_VALUE_DESC
                    ),
                    FilterUiEntity(
                        GradeSortType.BY_LESSON_NAME,
                        GradeSortType.toString(requireContext(), GradeSortType.BY_LESSON_NAME),
                        selectedItem == GradeSortType.BY_LESSON_NAME
                    ),
                )
                val bottomSheet = FilterBottomSheet(
                    requireContext().getString(R.string.sort_grades_by),
                    list,
                ) { id ->
                    gradesFilterViewModel.setGradeSort(requireContext(), id)
                }
                bottomSheet.show(
                    requireActivity().supportFragmentManager,
                    "sort_grade_bottom_sheet"
                )
            }
        }
    }

    override fun observeGrades() {
        gradesViewModel.grades.observe(viewLifecycleOwner) { list ->
            gradeAdapter.grades = list
        }
    }

    override fun observeGradesError() {
        gradesViewModel.errorMessage.observe(viewLifecycleOwner) {
            binding.grades.errorMessage.errorDescription.text = it
        }
    }

    override fun observeGradesLoadState() {
        val fadeThrough = MaterialFadeThrough()

        Singleton.updateGradeState.observe(viewLifecycleOwner) {
            with(binding.grades) {
                when (it) {
                    GradeUpdateStatus.UPDATE -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            gradesViewModel.load(requireContext())
                        }
                        TransitionManager.beginDelayedTransition(
                            loading.root,
                            fadeThrough
                        )
                        TransitionManager.beginDelayedTransition(
                            gradesRecyclerView,
                            fadeThrough
                        )

                        loading.root.startShimmer()
                        showLoading()
                    }

                    GradeUpdateStatus.ERROR -> {
                        loading.root.stopShimmer()
                        showError()
                    }

                    GradeUpdateStatus.FINISHED -> {
                        loading.root.stopShimmer()
                        if (gradesViewModel.grades.value.isNullOrEmpty()) {
                            emptyState.noHomeworkIcon.setImageDrawable(
                                AppCompatResources.getDrawable(
                                    requireContext(),
                                    R.drawable.ic_emty_grade
                                )
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
