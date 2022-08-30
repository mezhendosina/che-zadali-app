package com.mezhendosina.sgo.app.ui.adapters

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
import com.mezhendosina.sgo.data.DateManipulation
import com.mezhendosina.sgo.data.requests.diary.entities.DiaryEntity
import com.mezhendosina.sgo.data.requests.diary.entities.Lesson

typealias CurrentItemListener = () -> Int

class JournalPagerAdapter(
    private val navController: NavController,
    private val currentItemListener: CurrentItemListener,
) :
    PagingDataAdapter<DiaryAdapterEntity, JournalPagerAdapter.ViewHolder>(DiaryDiffCallback()) {
    class ViewHolder(val binding: ItemJournalViewpagerBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemJournalViewpagerBinding.inflate(inflater, parent, false)
        binding.weekSelectorLayout.root.setOnClickListener { refresh() }
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val diary = getItem(position)
        with(holder.binding) {
            if (diary != null) {
                this.weekSelectorLayout.weekSelectorTextView.text =
                    "${DateManipulation(diary.weekStart).journalDate()} - ${
                        DateManipulation(diary.weekEnd).journalDate()
                    }"
            }
            if (diary != null && diary.weekDays.isNotEmpty()) {
                val diaryAdapter = DiaryAdapter(object : OnHomeworkClickListener {
                    override fun invoke(p1: Lesson) {
                        val d = getItem(currentItemListener.invoke())
                        if (d != null) {
//                            Singleton.diaryEntity = d
                            navController.navigate(
                                R.id.action_containerFragment_to_lessonFragment,
                                bundleOf("lessonId" to p1.classmeetingId, "type" to "journal")
                            )
                        }
                    }
                })
                if (diary.pastMandatory.isNotEmpty()) {
                    val pastMandatoryAdapter = PastMandatoryAdapter()
                    pastMandatoryAdapter.items = diary.pastMandatory
                    pastMandatory.root.visibility = View.VISIBLE
                    pastMandatory.pastMandatoryRecyclerView.apply {
                        adapter = pastMandatoryAdapter
                        layoutManager = LinearLayoutManager(
                            holder.itemView.context,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        setRecycledViewPool(viewPool)
                    }
                } else pastMandatory.root.visibility = View.GONE
//
//                diaryAdapter.diary = diary.diaryResponse.weekDays
//                diaryAdapter.attachments = diary.attachmentsResponse



                this.diary.adapter = diaryAdapter
                this.diary.layoutManager =
                    LinearLayoutManager(
                        holder.itemView.context,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                this.diary.setRecycledViewPool(viewPool)
                emptyState.emptyText.visibility = View.INVISIBLE
                emptyState.noHomeworkIcon.visibility = View.INVISIBLE
                this.diary.visibility = View.VISIBLE
            } else {
                this.diary.visibility = View.INVISIBLE
                pastMandatory.root.visibility = View.INVISIBLE
                emptyState.noHomeworkIcon.visibility = View.VISIBLE
                emptyState.emptyText.visibility = View.VISIBLE
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
