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

package com.mezhendosina.sgo.app.ui.gradeItem

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ItemCountGradeBinding
import com.mezhendosina.sgo.app.model.grades.entities.CountGradeEntity
import com.mezhendosina.sgo.app.utils.GradesType
import com.mezhendosina.sgo.app.utils.setupColorWithGrade
import com.mezhendosina.sgo.app.utils.setupGrade

class CountGradeAdapter : RecyclerView.Adapter<CountGradeAdapter.CountGradeAdapterViewHolder>() {

    var countGrades = emptyList<CountGradeEntity>()
        set(value) {
            field = value.sortedBy { it.gradeName }.reversed()
            notifyDataSetChanged()
        }

    class CountGradeAdapterViewHolder(val binding: ItemCountGradeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountGradeAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCountGradeBinding.inflate(inflater, parent, false)

        return CountGradeAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountGradeAdapterViewHolder, position: Int) {
        val countGrade = countGrades[position]

        with(holder.binding) {
            header.text = countGrade.name
            value.value.text = countGrade.value.toString()
            value.setupGrade(
                holder.itemView.context,
                countGrade.gradeType,
                countGrade.value.toString(),
                true
            )

            header.setupColorWithGrade(
                holder.itemView.context,
                countGrade.gradeType
            )

            root.setBackgroundResource(
                when (countGrade.gradeType) {
                    GradesType.GOOD_GRADE -> R.drawable.shape_good_grade
                    GradesType.MID_GRADE -> R.drawable.shape_mid_grade
                    GradesType.BAD_GRADE -> R.drawable.shape_bad_grade
                    else -> 0
                }
            )
        }
    }

    override fun getItemCount(): Int = countGrades.size
}