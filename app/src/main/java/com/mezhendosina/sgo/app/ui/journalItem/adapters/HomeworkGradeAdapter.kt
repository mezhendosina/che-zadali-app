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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.ItemGradeValueBinding
import com.mezhendosina.sgo.app.model.journal.entities.AssignmentUiEntity
import com.mezhendosina.sgo.app.utils.setupGrade
import com.mezhendosina.sgo.app.utils.toGradeType

class HomeworkGradeAdapter : RecyclerView.Adapter<HomeworkGradeAdapter.HomeworkGradeViewHolder>() {

    var grades: List<AssignmentUiEntity> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class HomeworkGradeViewHolder(val binding: ItemGradeValueBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeworkGradeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemGradeValueBinding.inflate(inflater, parent, false)

        return HomeworkGradeViewHolder(binding)
    }


    override fun onBindViewHolder(holder: HomeworkGradeViewHolder, position: Int) {
        val grade = grades[position].mark
        with(holder.binding) {
            if (grade != null) {
                if (grade.dutyMark) {
                    dutyMark.visibility = View.VISIBLE
                } else {
                    this.setupGrade(
                        holder.itemView.context,
                        grade.mark.toFloat().toGradeType(),
                        grade.mark.toString()
                    )
                    dutyMark.visibility = View.INVISIBLE
                }

            }
        }
    }

    override fun getItemCount(): Int = grades.size
}