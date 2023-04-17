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

package com.mezhendosina.sgo.app.ui.changeDiaryStyle

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentSettingsDiaryItemDesignBinding
import com.mezhendosina.sgo.app.model.journal.DiaryStyle
import com.mezhendosina.sgo.app.model.journal.entities.LessonUiEntity
import com.mezhendosina.sgo.app.model.journal.entities.WeekDayUiEntity
import com.mezhendosina.sgo.app.ui.journalItem.adapters.DiaryAdapter
import com.mezhendosina.sgo.app.ui.journalItem.adapters.HomeworkAdapter
import com.mezhendosina.sgo.app.ui.journalItem.adapters.OnHomeworkClickListener
import com.mezhendosina.sgo.app.utils.setupCardDesign
import com.mezhendosina.sgo.app.utils.setupListDesign
import com.mezhendosina.sgo.data.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChangeDiaryStyleFragment : Fragment(R.layout.fragment_settings_diary_item_design) {

    private var binding: FragmentSettingsDiaryItemDesignBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSettingsDiaryItemDesignBinding.bind(view)

        val settings = Settings(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            settings.diaryStyle.collect {
                withContext(Dispatchers.Main) {
                    when (it) {
                        DiaryStyle.AS_CARD -> {

                            binding!!.diaryItem.homeworkRecyclerView.setupCardDesign()
                            binding!!.diaryDesignRadioGroup.check(binding!!.withDrawable.id)
                        }
                        DiaryStyle.AS_LIST -> {
                            binding!!.diaryItem.homeworkRecyclerView.setupListDesign()
                            binding!!.diaryDesignRadioGroup.check(binding!!.asList.id)
                        }
                    }
                }
            }
        }

        binding!!.diaryDesignRadioGroup.setOnCheckedChangeListener { _, i ->
            CoroutineScope(Dispatchers.Main).launch {
                when (i) {
                    binding!!.asList.id -> settings.editPreference(
                        Settings.DIARY_STYLE,
                        DiaryStyle.AS_LIST
                    )
                    binding!!.withDrawable.id -> settings.editPreference(
                        Settings.DIARY_STYLE,
                        DiaryStyle.AS_CARD
                    )
                }
            }
        }

        bindDiaryItem()
    }


    private fun bindDiaryItem() {
        val diaryItem =
            Singleton.loadedDiaryUiEntity.firstOrNull { diaryUiEntity ->
                diaryUiEntity.weekDays.firstOrNull { it.lessons.size > 2 } != null
            }?.weekDays?.first { it.lessons.size > 2 }

        val homeworkAdapter = HomeworkAdapter(object : OnHomeworkClickListener {
            override fun invoke(p1: LessonUiEntity, p2: View) {}
        })

        homeworkAdapter.lessons = diaryItem?.lessons?.slice(0..2) ?: emptyList()
        println(diaryItem?.lessons)
        with(binding!!.diaryItem) {
            day.text = diaryItem?.date
            homeworkRecyclerView.adapter = homeworkAdapter
            homeworkRecyclerView.layoutManager =
                LinearLayoutManager(requireContext())
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}