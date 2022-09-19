package com.mezhendosina.sgo.app.ui.journalItem

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentItemJournalBinding
import com.mezhendosina.sgo.app.model.journal.entities.LessonUiEntity
import com.mezhendosina.sgo.app.ui.adapters.PastMandatoryAdapter
import com.mezhendosina.sgo.data.WeekStartEndEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JournalItemFragment(
    private val weekStartEndEntity: WeekStartEndEntity,
    private val navController: NavController,
    private val onWeekTextClick: () -> Unit
) : Fragment(R.layout.fragment_item_journal) {

    private lateinit var binding: FragmentItemJournalBinding

    private val viewModel: JournalItemViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentItemJournalBinding.bind(view)

        binding.weekSelectorLayout.root.setOnClickListener { onWeekTextClick.invoke() }
        viewModel.getWeek(weekStartEndEntity)
        viewModel.week.observe(viewLifecycleOwner) { diaryItem ->
            with(binding) {
                if (diaryItem != null) {
                    weekSelectorLayout.weekSelectorTextView.text =
                        "${diaryItem.weekStart} - ${diaryItem.weekEnd}"

                    if (diaryItem.pastMandatory.isEmpty()) {
                        pastMandatory.root.visibility = View.GONE
                    } else {
                        pastMandatory.root.visibility = View.VISIBLE
                        val pastMandatoryAdapter = PastMandatoryAdapter()

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
                    CoroutineScope(Dispatchers.Main).launch {
                        if (diaryItem.weekDays.isNotEmpty()) {
                            val diaryAdapter = DiaryAdapter(object : OnHomeworkClickListener {
                                override fun invoke(p1: LessonUiEntity, p2: View) {
                                    Singleton.lesson = p1
                                    navController.navigate(
                                        R.id.action_containerFragment_to_lessonFragment,
                                        bundleOf(),
                                        null,
//                                        FragmentNavigatorExtras(p2 to p1.classmeetingId.toString())
                                    )
                                }
                            })
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
        observeLoading()
    }

    private fun observeLoading() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }
    }
}