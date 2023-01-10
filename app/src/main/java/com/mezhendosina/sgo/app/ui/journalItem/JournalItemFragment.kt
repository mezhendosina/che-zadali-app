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

package com.mezhendosina.sgo.app.ui.journalItem

import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.platform.MaterialFadeThrough
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentItemJournalBinding
import com.mezhendosina.sgo.app.findTopNavController
import com.mezhendosina.sgo.app.model.journal.entities.LessonUiEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JournalItemFragment : Fragment(R.layout.fragment_item_journal) {


    private var binding: FragmentItemJournalBinding? = null

    private val viewModel: JournalItemViewModel by viewModels()

    private val pastMandatoryAdapter = PastMandatoryAdapter {
        Singleton.lesson = null
        Singleton.pastMandatoryItem = it
        findTopNavController().navigate(R.id.action_containerFragment_to_lessonFragment)
    }
    private val diaryAdapter = DiaryAdapter(object : OnHomeworkClickListener {
        override fun invoke(p1: LessonUiEntity, p2: View) {
            Singleton.lesson = p1
            findTopNavController().navigate(R.id.action_containerFragment_to_lessonFragment)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getWeek(arguments?.getString(WEEK_START), arguments?.getString(WEEK_END))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentItemJournalBinding.bind(view)

        observeWeek()
        observeLoading()
        observeError()
    }


    override fun onDestroyView() {
        if (binding != null) {
            TransitionManager.endTransitions(binding!!.root)
            binding!!.pastMandatory.pastMandatoryRecyclerView.adapter = null
            binding!!.diary.adapter = null
        }
        binding = null
        super.onDestroyView()
    }

    private fun observeError() {
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            if (it.isNotEmpty() && binding != null) {
                binding!!.loading.root.visibility = View.GONE
                binding!!.loadError.root.visibility = View.VISIBLE
                binding!!.loadError.errorDescription.text = it
                binding!!.loadError.retryButton.setOnClickListener {
                    binding!!.loading.root.visibility = View.VISIBLE
                    binding!!.loadError.root.visibility = View.GONE
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.getWeek(
                            arguments?.getString(WEEK_START),
                            arguments?.getString(WEEK_END)
                        )
                    }
                }
            }
        }
    }

    private fun observeLoading() {
        val fadeThrough = MaterialFadeThrough()
        TransitionManager.beginDelayedTransition(binding?.root, fadeThrough)
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (binding != null) {
                if (it) {
                    binding!!.loading.root.visibility = View.VISIBLE
                    binding!!.loading.root.startShimmer()
                } else {
                    binding!!.loading.root.hideShimmer()
                    binding!!.loading.root.visibility = View.GONE
                }
            }
        }
    }

    private fun observeWeek() {
        viewModel.week.observe(viewLifecycleOwner) { diaryItem ->
            if (binding != null) {
                with(binding!!) {
                    if (diaryItem != null) {
                        if (diaryItem.pastMandatory.isEmpty()) {
                            pastMandatory.root.visibility = View.GONE
                        } else {
                            pastMandatory.root.visibility = View.VISIBLE
                            pastMandatoryAdapter.items = diaryItem.pastMandatory
                            pastMandatory.pastMandatoryRecyclerView.apply {
                                adapter = pastMandatoryAdapter
                                layoutManager = LinearLayoutManager(
                                    requireContext(),
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                            }
                        }
                        if (diaryItem.weekDays.isNotEmpty()) {
                            diaryAdapter.weekDays = diaryItem.weekDays
                            diary.adapter = diaryAdapter
                            diary.layoutManager =
                                LinearLayoutManager(
                                    requireContext(),
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                            diary.visibility = View.VISIBLE
                            emptyState.root.visibility = View.GONE
                        } else {
                            diary.visibility = View.GONE
                            emptyState.root.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }


    companion object {
        const val WEEK_START = "week_start"
        const val WEEK_END = "week_end"
    }
}