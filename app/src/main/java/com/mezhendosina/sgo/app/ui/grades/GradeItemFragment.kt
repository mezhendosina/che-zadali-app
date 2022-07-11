package com.mezhendosina.sgo.app.ui.grades

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.transition.MaterialSharedAxis
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentGradeItemBinding
import com.mezhendosina.sgo.app.databinding.GradeTypeBinding
import com.mezhendosina.sgo.app.findTopNavController
import com.mezhendosina.sgo.data.layouts.grades.GradesItem

class GradeItemFragment : Fragment(R.layout.fragment_grade_item) {

    lateinit var binding: FragmentGradeItemBinding

    lateinit var lesson: GradesItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lesson = Singleton.grades[arguments?.getInt("LESSON_INDEX") ?: 0]
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentGradeItemBinding.bind(view)
        binding.toolbar.setNavigationOnClickListener { findTopNavController().navigateUp() }
        binding.collapsingtoolbarlayout.title = lesson.name
        with(binding.avgGrade) {
            if (lesson.avg!! < 2.5) {
                badGrade.text = lesson.avg.toString()
                showBadGrade(this)
            } else if (2.5 <= lesson.avg!! && lesson.avg!! < 3.5) {
                midGrade.text = lesson.avg.toString()
                showMidGrade(this)
            } else if (lesson.avg!! >= 3.5) {
                goodGrade.text = lesson.avg.toString()
                showGoodGrade(this)
            }

            when (lesson.avg) {
                5.0 -> goodGrade.text = "5"
                4.0 -> goodGrade.text = "4"
                3.0 -> midGrade.text = "3"
                2.0 -> badGrade.text = "2"
                1.0 -> badGrade.text = "1"
            }
        }
        with(binding.countGrade) {
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
    }

    private fun showBadGrade(binding: GradeTypeBinding) {
        binding.apply {
            midGrade.visibility = View.GONE
            goodGrade.visibility = View.GONE
            goodGrade.text = ""
            badGrade.visibility = View.VISIBLE
        }
    }

    private fun showMidGrade(binding: GradeTypeBinding) {
        binding.apply {
            goodGrade.text = ""
            goodGrade.visibility = View.GONE
            badGrade.visibility = View.GONE
            midGrade.visibility = View.VISIBLE
        }
    }

    private fun showGoodGrade(binding: GradeTypeBinding) {
        binding.apply {
            goodGrade.visibility = View.VISIBLE
            midGrade.visibility = View.GONE
            badGrade.visibility = View.GONE
        }
    }
}