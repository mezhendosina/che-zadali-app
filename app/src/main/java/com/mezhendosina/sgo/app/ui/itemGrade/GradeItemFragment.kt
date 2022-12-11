package com.mezhendosina.sgo.app.ui.itemGrade

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentGradeItemBinding
import com.mezhendosina.sgo.app.databinding.ItemCountGradeBinding
import com.mezhendosina.sgo.app.findTopNavController
import com.mezhendosina.sgo.app.ui.adapters.bindGradeValue
import com.mezhendosina.sgo.data.requests.sgo.grades.entities.GradesItem

class GradeItemFragment : Fragment(R.layout.fragment_grade_item) {

    lateinit var binding: FragmentGradeItemBinding

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lesson = Singleton.grades[arguments?.getInt("LESSON_INDEX") ?: 0]
        viewModel.initCalculator(lesson)
        enterTransition = MaterialContainerTransform()
        returnTransition = MaterialContainerTransform()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGradeItemBinding.bind(view)
        binding.root.transitionName = lesson.name

        binding.collapsingtoolbarlayout.title = lesson.name
        binding.toolbar.setNavigationOnClickListener { findTopNavController().popBackStack() }

        binding.gradeCalculator.calculateGrade.adapter = calculateGradeAdapter
        binding.gradeCalculator.calculateGrade.layoutManager = LinearLayoutManager(requireContext())
        binding.gradeCalculator.calculateGrade.itemAnimator = null

        if (lesson.avg == "5,00") {
            binding.gradeCalculatorHeader.visibility = View.GONE
            binding.gradeCountDivider.visibility = View.GONE
            binding.gradeCalculator.root.visibility = View.GONE
        }

        bindGradeValue(lesson, binding.avgGrade)
        bindGradeCount(binding.countGrade)
        observeCalculatedGrade()
        observeChangeToGrade()
        observeOldCalculatedGrade()
    }

    private fun observeOldCalculatedGrade() {
        viewModel.oldCalculatedGrade.observe(viewLifecycleOwner) {
            with(binding.gradeOldCalculator) {
                if (it.countFive > 0) {
                    reqFiveCount.text = it.countFive.toString()
                    showHideFiveCount(View.VISIBLE)
                } else {
                    showHideFiveCount(View.GONE)
                }
                if (it.countFour > 0) {
                    reqFourCount.text = it.countFour.toString()
                    showHideFourCount(View.VISIBLE)
                } else {
                    showHideFourCount(View.GONE)
                }
                if (it.countThree > 0) {
                    reqThreeCount.text = it.countThree.toString()
                    showHideThreeCount(View.VISIBLE)
                } else {
                    showHideThreeCount(View.GONE)
                }
            }
        }
    }

    private fun observeChangeToGrade() {
        viewModel.oldChangeToGrade.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.gradeOldCalculator.calculatorSlider.value = it.toFloat()
                bindGradeValue(
                    GradesItem(
                        "",
                        null,
                        null,
                        null,
                        null,
                        null,
                        it.toString()
                    ),
                    binding.gradeCalculator.calculatedGrade
                )
            }
        }
    }

    private fun observeCalculatedGrade() {
        viewModel.calculatedGrade.observe(viewLifecycleOwner) {
            calculateGradeAdapter.grades = it.toList()
            calculateGradeAdapter.initGrades =
                viewModel.grade.value?.toList() ?: listOf(0, 0, 0, 0, 0)
            bindGradeValue(
                it.toGradeItem(),
                binding.gradeCalculator.calculatedGrade
            )
        }

    }

    private fun bindGradeCount(binding: ItemCountGradeBinding) = with(binding) {
        if (lesson.one == null) {
            oneH.visibility = View.GONE
            oneValue.visibility = View.GONE
        } else {
            oneH.visibility = View.VISIBLE
            oneValue.visibility = View.VISIBLE
            oneValue.text = lesson.one.toString()
        }

        if (lesson.two == null) {
            twoH.visibility = View.GONE
            twoValue.visibility = View.GONE
        } else {
            twoH.visibility = View.VISIBLE
            twoValue.visibility = View.VISIBLE
            twoValue.text = lesson.two.toString()
        }

        if (lesson.three == null) {
            threeH.visibility = View.GONE
            threeValue.visibility = View.GONE
        } else {
            threeH.visibility = View.VISIBLE
            threeValue.visibility = View.VISIBLE
            threeValue.text = lesson.three.toString()
        }

        if (lesson.four == null) {
            fourH.visibility = View.GONE
            fourValue.visibility = View.GONE
        } else {
            fourH.visibility = View.VISIBLE
            fourValue.visibility = View.VISIBLE
            fourValue.text = lesson.four.toString()
        }

        if (lesson.five == null) {
            fiveH.visibility = View.GONE
            fiveValue.visibility = View.GONE
        } else {
            fiveH.visibility = View.VISIBLE
            fiveValue.visibility = View.VISIBLE
            fiveValue.text = lesson.five.toString()
        }
    }

    private fun showHideFiveCount(visibility: Int) {
        with(binding.gradeOldCalculator) {
            reqFiveCount.visibility = visibility
            reqFiveCountHeader.visibility = visibility
            fiveOrFour.visibility = visibility
            fiveOrFourDivider.visibility = visibility
        }
    }

    private fun showHideFourCount(visibility: Int) {
        with(binding.gradeOldCalculator) {
            reqFourCount.visibility = visibility
            reqFourCountHeader.visibility = visibility
            fiveOrFour.visibility = visibility
            fiveOrFourDivider.visibility = visibility
            fourOrThree.visibility = visibility
            fourOrThreeDivider.visibility = visibility
        }
    }

    private fun showHideThreeCount(visibility: Int) {
        with(binding.gradeOldCalculator) {
            reqThreeCount.visibility = visibility
            reqThreeCountHeader.visibility = visibility
            fourOrThree.visibility = visibility
            fourOrThreeDivider.visibility = visibility
        }
    }

    companion object {
        const val FIVE_GRADE = 0
        const val FOUR_GRADE = 1
        const val THREE_GRADE = 2
        const val TWO_GRADE = 3
    }
}
