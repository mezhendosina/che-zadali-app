package com.mezhendosina.sgo.app.ui.journal

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mezhendosina.sgo.app.ui.journalItem.JournalItemFragment
import com.mezhendosina.sgo.data.WeekStartEndEntity

class JournalPagerAdapter(
    val fragment: Fragment,
) : FragmentStateAdapter(fragment) {

    var weeksList: List<WeekStartEndEntity> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
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
