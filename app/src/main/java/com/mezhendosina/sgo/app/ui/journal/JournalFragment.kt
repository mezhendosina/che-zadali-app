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
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.currentWeekStart
import com.mezhendosina.sgo.data.tabDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JournalFragment : Fragment(R.layout.fragment_journal) {

    private lateinit var binding: FragmentJournalBinding
    internal val viewModel: JournalViewModel by viewModels()

    private val weekNow = currentWeekStart()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentJournalBinding.bind(view)

        val adapter = JournalPagerAdapter(this)
        binding.journalPager.adapter = adapter

        binding.journalPager.offscreenPageLimit = 1

        binding.journalPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Singleton.currentWeek = position

                val week = viewModel.weeks.value?.get(position)?.toUI()
                binding.weekSelectorLayout.weekSelectorTextView.text =
                    "${week?.weekStart} - ${week?.weekEnd}"
            }
        })

        binding.weekSelectorLayout.root.setOnClickListener {
            binding.journalPager.currentItem =
                viewModel.weeks.value?.indexOf(viewModel.weeks.value!!.find { it.weekStart == weekNow })
                    ?: 0
        }

        CoroutineScope(Dispatchers.Main).launch {
            Settings(requireContext()).currentUserId.collect {
                binding.journalPager.invalidate()
            }
        }

        viewModel.weeks.observe(viewLifecycleOwner) { entityList ->
            adapter.weeksList = entityList
            TabLayoutMediator(binding.tabsLayout, binding.journalPager) { tab, position ->
                tab.text =
                    "${tabDate(entityList[position].weekStart)} - ${tabDate(entityList[position].weekEnd)}"
            }.attach()

            if (Singleton.currentWeek == null) binding.journalPager.setCurrentItem(
                entityList.indexOf(entityList.find { it.weekStart == weekNow }),
                false
            ) else binding.journalPager.setCurrentItem(Singleton.currentWeek!!, false)
        }
    }

}