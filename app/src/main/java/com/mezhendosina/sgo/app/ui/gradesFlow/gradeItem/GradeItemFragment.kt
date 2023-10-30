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

package com.mezhendosina.sgo.app.ui.gradesFlow.gradeItem

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentGradeItemBinding
import com.mezhendosina.sgo.app.utils.GradesType
import com.mezhendosina.sgo.app.utils.ItemOffsetDecoration
import com.mezhendosina.sgo.app.utils.findTopNavController
import com.mezhendosina.sgo.app.utils.setLessonEmoji
import com.mezhendosina.sgo.app.utils.setupColorWithGrade
import com.mezhendosina.sgo.app.utils.setupGrade
import com.mezhendosina.sgo.app.utils.toGradeType
import com.mezhendosina.sgo.data.netschool.api.grades.entities.GradesItem

class GradeItemFragment : Fragment(R.layout.fragment_grade_item) {

    var binding: FragmentGradeItemBinding? = null

    private lateinit var lesson: GradesItem

    internal val viewModel: GradeItemViewModel by viewModels()

    private val calculateGradeAdapter = CalculateGradeAdapter(object : ChangeGradeClickListener {
        override fun plusGrade(grade: Int) {
            viewModel.editGrade(grade, 1)
        }

        override fun minusGrade(grade: Int) {
            viewModel.editGrade(grade, -1)
        }

        override fun manualEditGrade(grade: Int, value: Int) {
            viewModel.editGrade(grade, value)
        }

    })

    private val countGradeAdapter = CountGradeAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            scrimColor = Color.TRANSPARENT
        }
        sharedElementReturnTransition = MaterialContainerTransform().apply {
            scrimColor = Color.TRANSPARENT
        }

        lesson = Singleton.grades[arguments?.getInt("LESSON_INDEX") ?: 0]
        viewModel.initCalculator(lesson)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGradeItemBinding.bind(view)
        with(binding!!.gradeToolbar) {
            collapsingtoolbarlayout.title = lesson.name
            itemToolbar.setNavigationOnClickListener { findTopNavController().popBackStack() }
            setLessonEmoji(requireContext(), lesson.name)
        }

        binding!!.gradeCalculator.calculateGrade.adapter = calculateGradeAdapter
        binding!!.gradeCalculator.calculateGrade.layoutManager =
            LinearLayoutManager(requireContext())
        binding!!.gradeCalculator.calculateGrade.itemAnimator = null

        if (Singleton.gradesWithWeight) binding!!.gradeCalculator.root.visibility = View.GONE

        countGradeAdapter.countGrades = lesson.countGradesToList()
        val itemOffsetDecoration = ItemOffsetDecoration(36)
        with(binding!!.countGrade) {
            adapter = countGradeAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
            addItemDecoration(itemOffsetDecoration)
        }

        setupAvgGrade()
        observeCalculatedGrade()
    }

    override fun onDestroyView() {
        binding!!.countGrade.adapter = null
        binding = null
        super.onDestroyView()
    }

    private fun setupAvgGrade() {
        val avgGradeType = lesson.avgGrade().toGradeType()
        binding!!.avgGrade.root.setBackgroundResource(
            when (avgGradeType) {
                GradesType.GOOD_GRADE -> R.drawable.shape_good_grade
                GradesType.MID_GRADE -> R.drawable.shape_mid_grade
                GradesType.BAD_GRADE -> R.drawable.shape_bad_grade
                else -> 0
            }
        )
        binding!!.avgGrade.avgHeader.setupColorWithGrade(requireContext(), avgGradeType)
        binding!!.avgGrade.avgGrade.setupGrade(
            requireContext(),
            avgGradeType,
            lesson.avg ?: "",
            true
        )
        binding!!.avgGrade.root.background.setBounds(41, 41, 41, 41)
    }


    private fun observeCalculatedGrade() {
        viewModel.calculatedGrade.observe(viewLifecycleOwner) {
            calculateGradeAdapter.grades = it.toList()
            calculateGradeAdapter.initGrades =
                viewModel.grade.value?.toList() ?: listOf(0, 0, 0, 0, 0)
            val avgGrade = it.avg()
            binding!!.gradeCalculator.calculatedGrade.setupGrade(
                requireContext(), avgGrade.toGradeType(), avgGrade.toString()
            )
        }
    }

    companion object {
        const val FIVE_GRADE = 0
        const val FOUR_GRADE = 1
        const val THREE_GRADE = 2
        const val TWO_GRADE = 3
    }
}
