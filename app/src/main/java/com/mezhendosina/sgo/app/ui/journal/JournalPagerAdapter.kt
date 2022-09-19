package com.mezhendosina.sgo.app.ui.journal

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mezhendosina.sgo.app.ui.journalItem.JournalItemFragment
import com.mezhendosina.sgo.data.WeekStartEndEntity

class JournalPagerAdapter(
    private val navController: NavController,
    val fragment: Fragment
) : FragmentStateAdapter(fragment) {

    var weeksList: List<WeekStartEndEntity> = emptyList()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = weeksList.size

    override fun createFragment(position: Int): Fragment =
        JournalItemFragment(
            weeksList[position],
            navController
        )
}