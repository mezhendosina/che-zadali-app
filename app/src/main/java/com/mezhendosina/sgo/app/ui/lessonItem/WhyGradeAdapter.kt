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

package com.mezhendosina.sgo.app.ui.lessonItem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.ItemWhyGradeBinding
import com.mezhendosina.sgo.app.ui.grades.showBadGrade
import com.mezhendosina.sgo.app.ui.grades.showGoodGrade
import com.mezhendosina.sgo.app.ui.grades.showMidGrade
import com.mezhendosina.sgo.data.requests.sgo.diary.entities.AssignmentTypesResponseEntity
import com.mezhendosina.sgo.data.requests.sgo.homework.entities.WhyGradeEntity

class WhyGradeAdapter : RecyclerView.Adapter<WhyGradeAdapter.WhyGradeViewHolder>() {

    var grades: List<WhyGradeEntity> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var types: List<AssignmentTypesResponseEntity> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class WhyGradeViewHolder(val binding: ItemWhyGradeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WhyGradeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWhyGradeBinding.inflate(inflater, parent, false)

        return WhyGradeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WhyGradeViewHolder, position: Int) {
        val grade = grades[position]
        with(holder.binding) {
            this.grade.apply {
                if (grade.mark.dutyMark) {
                    dutyMark.visibility = View.VISIBLE

                } else {
                    dutyMark.visibility = View.INVISIBLE

                    when (grade.mark.mark) {
                        2, 1 -> {
                            showBadGrade(this)
                            badGrade.text = grade.mark.mark.toString()
                        }
                        3 -> {
                            showMidGrade(this)
                            midGrade.text = grade.mark.mark.toString()
                        }
                        4, 5 -> {
                            showGoodGrade(this)
                            goodGrade.text = grade.mark.mark.toString()
                        }
                    }
                }
            }
            if (grade.assignmentName != "---Не указана---") {
                gradeText.text = grade.assignmentName
                gradeType.text = types.find { it.id == grade.typeId }?.name
                gradeType.visibility = View.VISIBLE
            } else {
                gradeText.text = types.find { it.id == grade.typeId }?.name
                gradeType.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int = grades.size
}