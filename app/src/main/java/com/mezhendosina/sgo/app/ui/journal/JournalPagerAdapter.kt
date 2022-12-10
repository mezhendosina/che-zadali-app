package com.mezhendosina.sgo.app.ui.journal

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mezhendosina.sgo.app.ui.journalItem.JournalItemFragment
import com.mezhendosina.sgo.data.WeekStartEndEntity

class JournalPagerAdapter(
    val fragment: FragmentManager,
    val lifecycle: Lifecycle
) : FragmentStateAdapter(fragment, lifecycle) {

    class DiffUtilCallback(
        private val oldItem: List<WeekStartEndEntity>,
        private val newItem: List<WeekStartEndEntity>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldItem.size

        override fun getNewListSize(): Int = newItem.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            newItem[newItemPosition].weekStart == oldItem[oldItemPosition].weekStart
    }

    var weeksList: List<WeekStartEndEntity> = emptyList()
        set(value) {
            val diffUtilCallback = DiffUtilCallback(field, value)
            val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun getItemCount(): Int = weeksList.size

    override fun createFragment(position: Int): Fragment {
        val weekStartEnd = weeksList[position]
        val bundle = bundleOf(
            JournalItemFragment.WEEK_START to weekStartEnd.weekStart,
            JournalItemFragment.WEEK_END to weekStartEnd.weekEnd
        )
        val f = JournalItemFragment()
        f.arguments = bundle
        return f
    }
}
