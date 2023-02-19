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

package com.mezhendosina.sgo.app.ui.journalItem.adapters

import android.os.Trace
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.ItemDiaryBinding
import com.mezhendosina.sgo.app.model.journal.entities.WeekDayUiEntity

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
        val day = weekDays[position]
        with(holder.binding) {
            Trace.beginSection("Binding homework")
            this@with.day.text = day.date

            holder.homeworkAdapter.lessons = day.lessons

            homeworkRecyclerView.apply {
                adapter = holder.homeworkAdapter
                layoutManager = holder.layoutManager
            }
            Trace.endSection()
        }
    }

    override fun getItemCount(): Int = weekDays.size
}

