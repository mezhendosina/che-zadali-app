/*
 * Copyright 2023 Eugene Menshenin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mezhendosina.sgo.app.ui.journalFlow.journal

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
import com.mezhendosina.sgo.data.SettingsDataStore
import com.mezhendosina.sgo.data.currentWeekStart
import com.mezhendosina.sgo.data.getValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.loadWeeks()
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
                if (tabLayout != null) {
                    tabLayoutMediator = TabLayoutMediator(
                        tabLayout,
                        binding!!.journalPager
                    ) { tab, position ->
                        tab.text =
                            "${entityList[position].formattedWeekStart} - ${entityList[position].formattedWeekEnd}"
                    }
                    tabLayoutMediator?.attach()
                }
                adapter.weeksList = entityList
                if (Singleton.currentWeek == null) {
                    val currentWeekIndex =
                        entityList.indexOf(entityList.firstOrNull { it.weekStart == weekNow })
                    binding!!.journalPager.setCurrentItem(
                        if (currentWeekIndex != -1) currentWeekIndex else entityList.lastIndex,
                        false
                    )
                } else {
                    binding!!.journalPager.setCurrentItem(Singleton.currentWeek!!, false)
                }
            }
        }
        observeUserId()
    }

    private fun observeUserId() {
        SettingsDataStore.CURRENT_USER_ID.getValue(requireContext(), -1).asLiveData()
            .observe(viewLifecycleOwner) {
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