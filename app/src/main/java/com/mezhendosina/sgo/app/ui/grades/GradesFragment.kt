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
import com.mezhendosina.sgo.app.findTopNavController
import com.mezhendosina.sgo.app.ui.adapters.GradeAdapter
import com.mezhendosina.sgo.app.ui.adapters.OnGradeClickListener
import com.mezhendosina.sgo.app.ui.hideAnimation
import com.mezhendosina.sgo.app.ui.showAnimation
import com.mezhendosina.sgo.data.requests.grades.entities.GradesItem

class GradesFragment : Fragment(R.layout.fragment_grades) {

    lateinit var binding: FragmentGradesBinding

    private val viewModel: GradesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.load(requireContext())

        exitTransition = MaterialFadeThrough()
        enterTransition = MaterialFadeThrough()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentGradesBinding.bind(view)


        val gradeAdapter = GradeAdapter(object : OnGradeClickListener {
            override fun invoke(p1: GradesItem) {
                val a = viewModel.grades.value?.indexOf(p1)
                findTopNavController().navigate(
                    R.id.action_containerFragment_to_gradeItemFragment,
                    bundleOf("LESSON_INDEX" to a)
                )
            }
        })

        binding.termSelector.setOnClickListener(onTermSelectedListener())

        viewModel.grades.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                gradeAdapter.grades = it
            } else {
                binding.emptyState.root.visibility = View.VISIBLE
                binding.emptyState.emptyText.text = "Оценок нету"
            }
        }

        observeErrors()
        observeLoading()
        observeTerms()

        binding.gradesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.gradesRecyclerView.adapter = gradeAdapter
    }

    private fun observeTerms() {
        viewModel.terms.observe(viewLifecycleOwner) {
            binding.termSelector.isVisible = !it.isNullOrEmpty()
            viewModel.setCurrentTerm(requireContext(), binding.termSelector, it)
        }
    }

    private fun observeLoading() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                showAnimation(binding.gradesProgressBar)
            } else {
                hideAnimation(binding.gradesProgressBar, View.INVISIBLE)
            }
        }
    }

    private fun observeErrors() {
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                binding.errorMessage.errorDescription.text = it
                showAnimation(binding.errorMessage.root)
                binding.errorMessage.retryButton.setOnClickListener {
                    viewModel.load(requireContext())
                    hideAnimation(binding.errorMessage.root, View.GONE)
                }
            }
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
                viewModel.terms.value?.map { it.name } ?: emptyList()
            )
        popup.setAdapter(adapter)

        popup.setOnItemClickListener { _, _, position, _ ->
            popup.dismiss()

            viewModel.reload(requireContext(), position)
            binding.termSelector.text = viewModel.terms.value?.get(position)?.name ?: ""
        }
        popup.show()
    }
}