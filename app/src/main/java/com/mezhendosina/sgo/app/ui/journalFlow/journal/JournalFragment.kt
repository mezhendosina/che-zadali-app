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
import androidx.lifecycle.asLiveData
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayoutMediator
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentJournalBinding
import com.mezhendosina.sgo.data.SettingsDataStore
import com.mezhendosina.sgo.data.currentWeekStart
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class JournalFragment : Fragment(R.layout.fragment_journal) {

    private var binding: FragmentJournalBinding? = null

    private val weekNow = currentWeekStart()

    private var tabLayoutMediator: TabLayoutMediator? = null

    @Inject lateinit var settingsDataStore: SettingsDataStore

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

        }
        observeUserId()
    }

    private fun observeUserId() {
        settingsDataStore.getValue(SettingsDataStore.CURRENT_USER_ID).asLiveData()
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