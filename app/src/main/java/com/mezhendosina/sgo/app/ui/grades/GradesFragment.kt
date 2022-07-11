package com.mezhendosina.sgo.app.ui.grades

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialFadeThrough
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentGradesBinding
import com.mezhendosina.sgo.app.factory
import com.mezhendosina.sgo.app.findTopNavController
import com.mezhendosina.sgo.app.ui.adapters.GradeAdapter
import com.mezhendosina.sgo.app.ui.adapters.OnGradeClickListener
import com.mezhendosina.sgo.data.layouts.grades.GradesItem

class GradesFragment : Fragment(R.layout.fragment_grades) {

    lateinit var binding: FragmentGradesBinding

    private val viewModel: GradesViewModel by viewModels { factory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exitTransition = MaterialFadeThrough()
        enterTransition = MaterialFadeThrough()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentGradesBinding.bind(view)

        viewModel.loadGrades(requireContext(), binding, true)

        val gradeAdapter = GradeAdapter(object : OnGradeClickListener {
            override fun invoke(p1: GradesItem) {
                val a = viewModel.grades.value?.indexOf(p1)
                findTopNavController().navigate(
                    R.id.action_containerFragment_to_gradeItemFragment,
                    bundleOf("LESSON_INDEX" to a)
                )
            }
        })


        viewModel.terms.observe(viewLifecycleOwner) {
            binding.termSelector.isVisible = it.isNotEmpty()
            viewModel.setCurrentTerm(requireContext(), binding.termSelector, it)
        }

        binding.termSelector.setOnClickListener { it1 ->
            val popup = ListPopupWindow(
                requireContext(),
                null,
                com.google.android.material.R.attr.listPopupWindowStyle
            )

            popup.anchorView = it1

            val adapter =
                ArrayAdapter(
                    requireContext(),
                    R.layout.list_popup_window_item,
                    viewModel.terms.value?.map { it.name } ?: emptyList()
                )
            popup.setAdapter(adapter)

            popup.setOnItemClickListener { _, _, position, _ ->
                popup.dismiss()

                viewModel.reloadGrades(requireContext(), binding, position)

            }
            popup.show()
        }

        viewModel.grades.observe(viewLifecycleOwner) {
//            println(it)
//            if (it.isNullOrEmpty()) {
//                binding.apply {
//                    hideAnimation(gradesRecyclerView, View.INVISIBLE)
//                    showAnimation(loadGradesText)
//                    showAnimation(gradesProgressBar)
//                }
//            } else {
//                binding.apply {
//                    hideAnimation(loadGradesText, View.GONE)
//                    hideAnimation(gradesProgressBar, View.GONE)
//                    showAnimation(gradesRecyclerView)
//                }
//            }
            gradeAdapter.grades = it
        }
        binding.gradesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.gradesRecyclerView.adapter = gradeAdapter
    }
}