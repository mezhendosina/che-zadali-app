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

package com.mezhendosina.sgo.app.ui.grades

import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.platform.MaterialFadeThrough
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentGradesBinding
import com.mezhendosina.sgo.app.findTopNavController
import com.mezhendosina.sgo.app.ui.adapters.GradeAdapter
import com.mezhendosina.sgo.app.ui.adapters.OnGradeClickListener
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.requests.sgo.grades.entities.GradesItem

class GradesFragment : Fragment(R.layout.fragment_grades) {

    private var binding: FragmentGradesBinding? = null

    internal val viewModel: GradesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
    }

    private val gradeAdapter = GradeAdapter(object : OnGradeClickListener {
        override fun invoke(p1: GradesItem, p2: View) {
            val a = viewModel.grades.value?.indexOf(p1)
            findTopNavController().navigate(
                R.id.action_containerFragment_to_gradeItemFragment,
                bundleOf("LESSON_INDEX" to a),
                null,
                FragmentNavigatorExtras(p2 to p1.name),
            )
        }
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGradesBinding.bind(view)

        binding!!.gradesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding!!.gradesRecyclerView.adapter = gradeAdapter

        observeGrades()
        observeErrors()
        observeLoading()
        observeReload()

    }

    override fun onDestroy() {
        super.onDestroy()
        if (binding != null) {
            TransitionManager.endTransitions(binding!!.mainLayout)
            binding!!.gradesRecyclerView.adapter = null
        }
        binding = null
    }


    private fun observeGrades() {
        viewModel.grades.observe(viewLifecycleOwner) { list ->
            if (binding != null) {
                if (list.any { !it.avg.isNullOrEmpty() }) {
                    gradeAdapter.grades = list
                    binding!!.emptyState.root.visibility = View.GONE
                    binding!!.gradesRecyclerView.visibility = View.VISIBLE
                } else {
                    binding!!.emptyState.root.visibility = View.VISIBLE
                    binding!!.gradesRecyclerView.visibility = View.GONE
                    binding!!.emptyState.emptyText.text = "Оценок нет"
                }
                binding!!.gradesRecyclerView.doOnPreDraw {
                    Singleton.transition.value = true
                }
            }
        }
    }

    private fun observeLoading() {
        val fadeThrough = MaterialFadeThrough()
        if (binding != null) {
            TransitionManager.beginDelayedTransition(binding!!.mainLayout, fadeThrough)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (binding != null) {
                if (it) {
                    binding!!.loading.root.visibility = View.VISIBLE
                    binding!!.gradesRecyclerView.visibility = View.INVISIBLE
                    binding!!.loading.root.startShimmer()
                } else {
                    binding!!.loading.root.visibility = View.GONE
                    binding!!.loading.root.stopShimmer()
                }
            }
        }
    }

    private fun observeErrors() {
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty() && binding != null) {
                binding!!.errorMessage.errorDescription.text = it
                binding!!.errorMessage.retryButton.setOnClickListener {
                    viewModel.load(requireContext())
                    binding!!.errorMessage.root.visibility = View.GONE
                }
            }
        }
    }

    private fun observeReload() {
        val settings = Settings(requireContext())

        settings.currentTrimId.asLiveData().observe(viewLifecycleOwner) {
            viewModel.load(requireContext())
        }
    }
}