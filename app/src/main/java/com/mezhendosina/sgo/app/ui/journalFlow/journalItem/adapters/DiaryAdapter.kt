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

package com.mezhendosina.sgo.app.ui.journalFlow.journalItem.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.databinding.ItemDiaryBinding
import com.mezhendosina.sgo.app.model.journal.DiaryStyle
import com.mezhendosina.sgo.app.model.journal.entities.WeekDayUiEntity
import com.mezhendosina.sgo.app.utils.setupCardDesign
import com.mezhendosina.sgo.app.utils.setupListDesign

class DiaryAdapter(
    private val onHomeworkClickListener: OnHomeworkClickListener
) : RecyclerView.Adapter<DiaryAdapter.ViewHolder>() {

    var weekDays: List<WeekDayUiEntity> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }


    class ViewHolder(
        val binding: ItemDiaryBinding,
        onHomeworkClickListener: OnHomeworkClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        val homeworkAdapter = HomeworkAdapter(onHomeworkClickListener)

        val layoutManager = LinearLayoutManager(
            itemView.context,
            LinearLayoutManager.VERTICAL,
            false
        )


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDiaryBinding.inflate(inflater, parent, false)

        return ViewHolder(binding, onHomeworkClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val diaryItem = weekDays[position]
        holder.binding.day.text = diaryItem.date
        holder.homeworkAdapter.lessons = diaryItem.lessons

        when (Singleton.diaryStyle.value) {
            DiaryStyle.AS_CARD -> holder.binding.homeworkRecyclerView.setupCardDesign()
            DiaryStyle.AS_LIST -> holder.binding.homeworkRecyclerView.setupListDesign()
        }
        holder.binding.homeworkRecyclerView.apply {
            adapter = holder.homeworkAdapter
            layoutManager = holder.layoutManager
        }

    }

    override fun getItemCount(): Int = weekDays.size
}

