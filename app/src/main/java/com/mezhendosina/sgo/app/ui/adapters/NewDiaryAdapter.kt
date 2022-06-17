package com.mezhendosina.sgo.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mezhendosina.sgo.app.databinding.JournalViewpagerItemBinding
import com.mezhendosina.sgo.data.layouts.diary.Diary
import kotlinx.coroutines.flow.Flow


class NewDiaryAdapter(
    private val onHomeworkClickListener: OnHomeworkClickListener,
    private val week: Flow<String>
) :
    PagingDataAdapter<Diary, NewDiaryAdapter.ViewHolder>(DiaryDiffCallback()) {
    class ViewHolder(val binding: JournalViewpagerItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = JournalViewpagerItemBinding.inflate(inflater, parent, false)

        binding.swipeRefresh.setOnRefreshListener { refresh() }

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val diary = getItem(position)
        with(holder.binding) {
            if (diary != null && diary.diaryResponse.weekDays.isNotEmpty()) {
                val diaryAdapter = DiaryAdapter(onHomeworkClickListener)
                diaryAdapter.diary = diary.diaryResponse.weekDays
                this.diary.adapter = diaryAdapter
                this.diary.layoutManager =
                    LinearLayoutManager(
                        holder.itemView.context,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                noHomework.visibility = View.INVISIBLE
                noHomeworkIcon.visibility = View.INVISIBLE
                this.diary.visibility = View.VISIBLE
            } else {
                this.diary.visibility = View.INVISIBLE
                noHomeworkIcon.visibility = View.VISIBLE
                noHomework.visibility = View.VISIBLE
            }
        }
    }
}

class DiaryDiffCallback : DiffUtil.ItemCallback<Diary>() {
    override fun areItemsTheSame(oldItem: Diary, newItem: Diary): Boolean {
        return oldItem.diaryResponse.weekStart == newItem.diaryResponse.weekStart
    }

    override fun areContentsTheSame(oldItem: Diary, newItem: Diary): Boolean {
        return oldItem == newItem
    }
}
