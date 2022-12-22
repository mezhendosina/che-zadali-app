package com.mezhendosina.sgo.app.ui.journal

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayoutMediator
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentJournalBinding
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.currentWeekStart
import com.mezhendosina.sgo.data.tabDate

class JournalFragment : Fragment(R.layout.fragment_journal) {

    private var binding: FragmentJournalBinding? = null
    internal val viewModel: JournalViewModel by viewModels()

    private val weekNow = currentWeekStart()

    private var tabLayoutMediator: TabLayoutMediator? = null

    private val onPageChangeCallback = object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            Singleton.currentWeek = position
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentJournalBinding.bind(view)

        val adapter = JournalPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        if (binding != null) {
            binding!!.journalPager.adapter = adapter
            binding!!.journalPager.registerOnPageChangeCallback(onPageChangeCallback)

            val tabLayout = Singleton.journalTabsLayout
            viewModel.weeks.observe(viewLifecycleOwner) { entityList ->
                adapter.weeksList = entityList
                if (Singleton.currentWeek == null) binding!!.journalPager.setCurrentItem(
                    entityList.indexOf(entityList.find { it.weekStart == weekNow }),
                    false
                ) else binding!!.journalPager.setCurrentItem(Singleton.currentWeek!!, false)

                if (tabLayout != null) {
                    tabLayoutMediator = TabLayoutMediator(
                        tabLayout,
                        binding!!.journalPager
                    ) { tab, position ->
                        tab.text =
                            "${tabDate(entityList[position].weekStart)} - ${tabDate(entityList[position].weekEnd)}"
                    }
                    tabLayoutMediator?.attach()
                }
            }
        }

        observeUserId()
    }

    private fun observeUserId() {
        Settings(requireContext()).currentUserId.asLiveData().observe(viewLifecycleOwner) {
            if (binding != null)
                binding!!.journalPager.invalidate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (binding != null) {
            binding!!.journalPager.adapter = null
            binding!!.journalPager.unregisterOnPageChangeCallback(onPageChangeCallback)
            tabLayoutMediator?.detach()
            tabLayoutMediator = null
            binding = null
        }
    }
}