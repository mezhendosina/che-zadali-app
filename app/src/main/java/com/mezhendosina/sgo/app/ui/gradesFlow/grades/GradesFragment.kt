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

package com.mezhendosina.sgo.app.ui.gradesFlow.grades

import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.platform.MaterialFadeThrough
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentGradesBinding
import com.mezhendosina.sgo.app.utils.GradeUpdateStatus
import com.mezhendosina.sgo.app.utils.findTopNavController
import com.mezhendosina.sgo.data.netschool.api.grades.entities.GradesItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GradesFragment : Fragment(R.layout.fragment_grades) {

    private var binding: FragmentGradesBinding? = null

    internal val viewModel: GradesViewModel by viewModels()


    private val gradeAdapter = GradeAdapter(object : OnGradeClickListener {
        override fun invoke(p1: GradesItem, p2: View) {
            val a = viewModel.grades.value?.indexOf(p1)

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGradesBinding.bind(view)

        binding!!.gradesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding!!.gradesRecyclerView.adapter = gradeAdapter

        binding!!.errorMessage.retryButton.setOnClickListener {
            Singleton.updateGradeState.value = GradeUpdateStatus.UPDATE
        }

        observeGrades()
        observeErrors()
        observeGradeState()
    }

    override fun onDestroyView() {
        if (binding != null) {
            TransitionManager.endTransitions(binding!!.mainLayout)
            binding!!.gradesRecyclerView.invalidate()
            binding!!.gradesRecyclerView.adapter = null
        }
        binding = null
        super.onDestroyView()
    }


    private fun observeGrades() {
        viewModel.grades.observe(viewLifecycleOwner) { list ->
            gradeAdapter.grades = list

        }
    }


    private fun observeErrors() {
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            binding!!.errorMessage.errorDescription.text = it
        }
    }

    private fun observeGradeState() {
        val fadeThrough = MaterialFadeThrough()

        Singleton.updateGradeState.observe(viewLifecycleOwner) {
            when (it) {
                GradeUpdateStatus.UPDATE -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.load(requireContext())
                    }
                    if (binding != null) {
                        TransitionManager.beginDelayedTransition(
                            binding!!.loading.root,
                            fadeThrough
                        )
                        TransitionManager.beginDelayedTransition(
                            binding!!.gradesRecyclerView,
                            fadeThrough
                        )


                        binding!!.loading.root.startShimmer()
                        binding!!.showLoading()
                    }
                }

                GradeUpdateStatus.ERROR -> {
                    if (binding != null) {
                        binding!!.loading.root.stopShimmer()
                        binding!!.showError()
                    }
                }

                GradeUpdateStatus.FINISHED -> {
                    if (binding != null) {
                        binding!!.loading.root.stopShimmer()
                        if (viewModel.grades.value.isNullOrEmpty()) {
                            binding!!.emptyState.noHomeworkIcon.setImageDrawable(
                                AppCompatResources.getDrawable(
                                    requireContext(),
                                    R.drawable.ic_emty_grade
                                )
                            )
                            binding!!.emptyState.emptyText.text = "Оценок нет"
                            binding!!.showEmptyState()
                        } else {
                            binding!!.gradesRecyclerView.doOnPreDraw {
                                binding!!.showGrades()
                            }
                        }
                    }
                }

                else -> {}
            }
        }
    }

    private fun FragmentGradesBinding.showGrades() {
        gradesRecyclerView.visibility = View.VISIBLE
        emptyState.root.visibility = View.INVISIBLE
        loading.root.visibility = View.INVISIBLE
        errorMessage.root.visibility = View.INVISIBLE
    }

    private fun FragmentGradesBinding.showEmptyState() {
        emptyState.root.visibility = View.VISIBLE
        gradesRecyclerView.visibility = View.INVISIBLE
        loading.root.visibility = View.INVISIBLE
        errorMessage.root.visibility = View.INVISIBLE
    }

    private fun FragmentGradesBinding.showLoading() {
        loading.root.visibility = View.VISIBLE
        gradesRecyclerView.visibility = View.INVISIBLE
        emptyState.root.visibility = View.INVISIBLE
        errorMessage.root.visibility = View.INVISIBLE
    }

    private fun FragmentGradesBinding.showError() {
        errorMessage.root.visibility = View.VISIBLE
        gradesRecyclerView.visibility = View.INVISIBLE
        emptyState.root.visibility = View.INVISIBLE
        loading.root.visibility = View.INVISIBLE
    }
}
