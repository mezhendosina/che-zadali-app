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

package com.mezhendosina.sgo.app.ui.gradesFlow.gradesFilter

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.BottomSheetFilterGradesBinding
import com.mezhendosina.sgo.app.model.grades.GradeSortType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Deprecated("BottomSheet's ui implemented in chips on grades screen")
class GradesFilterBottomSheet : BottomSheetDialogFragment(R.layout.bottom_sheet_filter_grades) {

    private lateinit var binding: BottomSheetFilterGradesBinding

    private val viewModel: GradesFilterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getGradeSort(requireContext())
            viewModel.getYearsList()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = BottomSheetFilterGradesBinding.bind(view)

        setupSortRadioGroup(requireContext())

        onYearClickListener()
        observeSelectedYear()
        observeLoading()
        observeDoneButtonClick()
    }

    private fun observeDoneButtonClick() {
        binding.done.setOnClickListener { this.dismiss() }
    }

    private fun observeLoading() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.yearValueShimmer.startShimmer()
            } else {
                binding.yearValueShimmer.hideShimmer()
            }
        }
    }


    private fun observeSelectedYear() {
        viewModel.currentYear.observe(viewLifecycleOwner) {
            binding.yearValue.text = GradesFilterViewModel.filterYearName(it.name)
        }
    }

    private fun setupSortRadioGroup(context: Context) {
        binding.sortGradeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.setGradeSort(
                context,
                when (checkedId) {
                    R.id.from_good_to_bad -> GradeSortType.BY_GRADE_VALUE
                    R.id.from_bad_to_good -> GradeSortType.BY_GRADE_VALUE_DESC
                    R.id.by_lesson_name -> GradeSortType.BY_LESSON_NAME
                    else -> GradeSortType.BY_LESSON_NAME
                }
            )
        }
    }

    private fun onYearClickListener() {
        binding.year.setOnClickListener {
            // мне стыдно за следующие 17 строк
            val items =
                viewModel.yearList.value?.map { GradesFilterViewModel.filterYearName(it.name) }
                    ?.toTypedArray()
            val selectedYearId = viewModel.currentYear.value
            val selectedYearName = GradesFilterViewModel.filterYearName(selectedYearId!!.name)
            val selectedYearArrayIndex = items?.indexOf(selectedYearName)

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.year_grade_header)
                .setSingleChoiceItems(items, selectedYearArrayIndex ?: -1) { _, which ->
                    val selectedId = viewModel.yearList.value?.firstOrNull {
                        it.name.contains(
                            items?.get(which) ?: "-1"
                        )
                    }!!
                    viewModel.changeSelectedYear(
                        selectedId
                    )
                }
                .setPositiveButton("Ок") { dialog, _ ->
                    dialog.dismiss()
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.updateYear()
                    }
                }
                .create()
                .show()
        }
    }


    companion object {
        const val TAG = "gradeFilterBottomSheet"
    }
}