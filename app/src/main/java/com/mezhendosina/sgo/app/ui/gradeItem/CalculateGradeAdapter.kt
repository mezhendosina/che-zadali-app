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

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ItemChangeCalculatedGradeBinding

interface ChangeGradeClickListener {

    fun plusGrade(grade: Int)

    fun minusGrade(grade: Int)

    fun manualEditGrade(grade: Int, value: Int)
}

class CalculateGradeAdapter(
    private val changeGradeClickListener: ChangeGradeClickListener
) : RecyclerView.Adapter<CalculateGradeAdapter.ViewHolder>(), View.OnClickListener {

    private class DiffUtilCallback(
        private val oldList: List<Int>,
        private val newList: List<Int>,
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItemPosition == newItemPosition
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]

    }

    var initGrades: List<Int> = emptyList()

    var grades: List<Int> = emptyList()
        set(value) {
            val diffUtilCallback = DiffUtilCallback(field, value)
            val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onClick(v: View) {
        val grade = v.tag as Int
        when (v.id) {
            R.id.plus_grade -> changeGradeClickListener.plusGrade(grade)
            R.id.minus_grade -> changeGradeClickListener.minusGrade(grade)
        }
    }

    class ViewHolder(val binding: ItemChangeCalculatedGradeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChangeCalculatedGradeBinding.inflate(inflater, parent, false)

        binding.plusGrade.setOnClickListener(this)
        binding.minusGrade.setOnClickListener(this)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val grade = grades[position]
        val initGrade = initGrades[position]
        with(holder.binding) {
            plusGrade.tag = position
            minusGrade.tag = position
            minusGrade.isEnabled = grade - initGrade > 0
            val deltaGrade = grade - initGrade

            val context = holder.itemView.context

            val gradeColor = TypedValue()
            var headerText: CharSequence = ""


            when (position) {
                GradeItemFragment.FIVE_GRADE -> {
                    headerText = context.getText(R.string.five_grade)
                    context.theme.resolveAttribute(R.attr.colorGoodGrade, gradeColor, true)
                }

                GradeItemFragment.FOUR_GRADE -> {
                    headerText = context.getText(R.string.four_grade)
                    context.theme.resolveAttribute(R.attr.colorGoodGrade, gradeColor, true)
                }

                GradeItemFragment.THREE_GRADE -> {
                    headerText = context.getText(R.string.three_grade)
                    context.theme.resolveAttribute(R.attr.colorMidGrade, gradeColor, true)
                }

                GradeItemFragment.TWO_GRADE -> {
                    headerText = context.getText(R.string.two_grade)
                    context.theme.resolveAttribute(
                        com.google.android.material.R.attr.colorError,
                        gradeColor,
                        true
                    )
                }
            }
            gradeValue.setText(deltaGrade.toString())
            header.text = headerText

            gradeValue.setTextColor(gradeColor.data)
            header.setTextColor(gradeColor.data)
        }
    }

    override fun getItemCount(): Int = grades.size

}