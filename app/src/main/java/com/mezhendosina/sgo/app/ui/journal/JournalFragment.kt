package com.mezhendosina.sgo.app.ui.journal

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.MaterialFadeThrough
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentJournalBinding
import com.mezhendosina.sgo.app.findTopNavController
import com.mezhendosina.sgo.data.currentWeekStart
import com.mezhendosina.sgo.data.tabDate

class JournalFragment : Fragment(R.layout.fragment_journal) {

    private lateinit var binding: FragmentJournalBinding
    private val viewModel: JournalViewModel by viewModels()

    private val weekNow = currentWeekStart()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentJournalBinding.bind(view)

        val adapter = JournalPagerAdapter(findTopNavController(), this) {
            binding.journalPager.currentItem =
                viewModel.weeks.value?.indexOf(viewModel.weeks.value!!.find { it.weekStart == weekNow })
                    ?: 0
        }
        binding.journalPager.adapter = adapter

        binding.journalPager.offscreenPageLimit = 1

        binding.journalPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Singleton.currentWeek = position
            }
        })

        viewModel.weeks.observe(viewLifecycleOwner) { entityList ->
            adapter.weeksList = entityList
            TabLayoutMediator(binding.tabsLayout, binding.journalPager) { tab, position ->
                tab.text = tabDate(entityList[position].weekStart)
            }
            if (Singleton.currentWeek == null) binding.journalPager.setCurrentItem(
                entityList.indexOf(entityList.find { it.weekStart == weekNow }),
                false
            ) else binding.journalPager.setCurrentItem(Singleton.currentWeek!!, false)
        }
    }
}