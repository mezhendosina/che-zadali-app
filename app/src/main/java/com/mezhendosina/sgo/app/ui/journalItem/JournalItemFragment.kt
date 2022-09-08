package com.mezhendosina.sgo.app.ui.journalItem

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentItemJournalBinding
import com.mezhendosina.sgo.app.databinding.FragmentJournalBinding
import com.mezhendosina.sgo.app.model.journal.entities.AdapterWeekDay
import com.mezhendosina.sgo.app.model.journal.entities.DiaryAdapterEntity
import com.mezhendosina.sgo.app.model.journal.entities.LessonAdapter
import com.mezhendosina.sgo.app.ui.adapters.PastMandatoryAdapter
import com.mezhendosina.sgo.app.ui.journal.JournalPagerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JournalItemFragment(
    private val diaryItem: DiaryAdapterEntity?,
    private val navController: NavController
) : Fragment(R.layout.fragment_item_journal) {

    private lateinit var binding: FragmentItemJournalBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentItemJournalBinding.bind(view)
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
                        setRecycledViewPool(JournalPagerAdapter.viewPool)
                    }

                }
                CoroutineScope(Dispatchers.Main).launch {
                    if (diaryItem.weekDays.isNotEmpty()) {
                        val diaryAdapter = DiaryAdapter(object : OnHomeworkClickListener {
                            override fun invoke(p1: LessonAdapter) {
                                Singleton.diaryEntity = diaryItem
                                navController.navigate(
                                    R.id.action_containerFragment_to_lessonFragment,
                                    bundleOf(
                                        "lessonId" to p1.classmeetingId,
                                        "type" to "journal"
                                    )
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
                        diary.setRecycledViewPool(JournalPagerAdapter.viewPool)
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