package com.mezhendosina.sgo.app.ui.journal

import android.os.Trace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ItemJournalViewpagerBinding
import com.mezhendosina.sgo.app.model.journal.entities.DiaryAdapterEntity
import com.mezhendosina.sgo.app.model.journal.entities.LessonAdapter
import com.mezhendosina.sgo.app.ui.adapters.PastMandatoryAdapter
import com.mezhendosina.sgo.app.ui.journalItem.DiaryAdapter
import com.mezhendosina.sgo.app.ui.journalItem.OnHomeworkClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

typealias CurrentItemListener = () -> Int


class JournalPagerAdapter(
    private val navController: NavController,
    private val currentItemListener: CurrentItemListener,
) :
    PagingDataAdapter<DiaryAdapterEntity, JournalPagerAdapter.ViewHolder>(DiaryDiffCallback()) {

    companion object {
        val viewPool = RecyclerView.RecycledViewPool()
    }

    class ViewHolder(val binding: ItemJournalViewpagerBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemJournalViewpagerBinding.inflate(inflater, parent, false)

        binding.weekSelectorLayout.root.setOnClickListener { refresh() }

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val diaryItem = getItem(position)
        with(holder.binding) {
            CoroutineScope(Dispatchers.Main).launch {

                val diaryAdapter = DiaryAdapter(object : OnHomeworkClickListener {
                    override fun invoke(p1: LessonAdapter) {
                        val d = getItem(currentItemListener.invoke())
                        if (d != null) {
                            Singleton.diaryEntity = d
                            navController.navigate(
                                R.id.action_containerFragment_to_lessonFragment,
                                bundleOf("lessonId" to p1.classmeetingId, "type" to "journal")
                            )
                        }
                    }
                })
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
                                holder.itemView.context,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                            setRecycledViewPool(viewPool)
                        }

                    }
                    if (diaryItem.weekDays.isNotEmpty()) {
                        diaryAdapter.weekDays = diaryItem.weekDays
                        diary.adapter = diaryAdapter
                        diary.layoutManager =
                            LinearLayoutManager(
                                holder.itemView.context,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                        diary.setRecycledViewPool(viewPool)
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

class DiaryDiffCallback : DiffUtil.ItemCallback<DiaryAdapterEntity>() {
    override fun areItemsTheSame(
        oldItem: DiaryAdapterEntity,
        newItem: DiaryAdapterEntity
    ): Boolean {
        return oldItem.weekStart == newItem.weekStart
    }

    override fun areContentsTheSame(
        oldItem: DiaryAdapterEntity,
        newItem: DiaryAdapterEntity
    ): Boolean {
        return oldItem == newItem
    }
}
