package com.mezhendosina.sgo.app.ui.itemGrade

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.transition.MaterialContainerTransform
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentGradeItemBinding
import com.mezhendosina.sgo.app.databinding.ItemCountGradeBinding
import com.mezhendosina.sgo.app.findTopNavController
import com.mezhendosina.sgo.app.ui.adapters.bindGradeValue
import com.mezhendosina.sgo.data.requests.grades.entities.GradesItem

class GradeItemFragment : Fragment(R.layout.fragment_grade_item) {

    lateinit var binding: FragmentGradeItemBinding

    private lateinit var lesson: GradesItem

    private val viewModel: GradeItemViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lesson = Singleton.grades[arguments?.getInt("LESSON_INDEX") ?: 0]
        viewModel.initCalculator(lesson)
        sharedElementEnterTransition = MaterialContainerTransform()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGradeItemBinding.bind(view)

        binding.root.transitionName = lesson.name

        binding.collapsingtoolbarlayout.title = lesson.name
        binding.toolbar.setNavigationOnClickListener { findTopNavController().popBackStack() }

        binding.gradeCalculator.calculatorSlider.addOnChangeListener { _, value, _ ->
            viewModel.calculateGrade(value)
        }

        if (lesson.avg == "5,00")
            binding.gradeCalculator.root.visibility = View.GONE

        bindGradeValue(lesson, binding.avgGrade)
        bindGradeCount(binding.countGrade)
        observeChangeToGrade()
        observeCalculatedGrade()
        observeCalculateError()
    }

    private fun observeCalculatedGrade() {
        viewModel.changeGradeItem.observe(viewLifecycleOwner) {
            with(binding.gradeCalculator) {
                if (it.five > 0) {
                    reqFiveCount.text = it.five.toString()
                    showHideFiveCount(View.VISIBLE)
                } else {
                    showHideFiveCount(View.GONE)
                }
                if (it.four > 0) {
                    reqFourCount.text = it.four.toString()
                    showHideFourCount(View.VISIBLE)
                } else {
                    showHideFourCount(View.GONE)
                }
                if (it.three > 0) {
                    reqThreeCount.text = it.three.toString()
                    showHideThreeCount(View.VISIBLE)
                } else {
                    showHideThreeCount(View.GONE)
                }
            }
        }
    }

    private fun observeChangeToGrade() {
        viewModel.changeToGrade.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.gradeCalculator.calculatorSlider.value = it.toFloat()
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

    private fun observeCalculateError() {
        viewModel.tooManyGrades.observe(viewLifecycleOwner) {
            if (it) {
                binding.gradeCalculator.tooManyGradesError.visibility = View.VISIBLE
            } else {
                binding.gradeCalculator.tooManyGradesError.visibility = View.GONE
            }
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
        with(binding.gradeCalculator) {
            reqFiveCount.visibility = visibility
            reqFiveCountHeader.visibility = visibility
            fiveOrFour.visibility = visibility
            fiveOrFourDivider.visibility = visibility
        }
    }

    private fun showHideFourCount(visibility: Int) {
        with(binding.gradeCalculator) {
            reqFourCount.visibility = visibility
            reqFourCountHeader.visibility = visibility
            fiveOrFour.visibility = visibility
            fiveOrFourDivider.visibility = visibility
            fourOrThree.visibility = visibility
            fourOrThreeDivider.visibility = visibility
        }
    }

    private fun showHideThreeCount(visibility: Int) {
        with(binding.gradeCalculator) {
            reqThreeCount.visibility = visibility
            reqThreeCountHeader.visibility = visibility
            fourOrThree.visibility = visibility
            fourOrThreeDivider.visibility = visibility
        }
    }
}
