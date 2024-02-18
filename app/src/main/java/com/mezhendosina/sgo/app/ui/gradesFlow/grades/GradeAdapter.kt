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

package com.mezhendosina.sgo.app.ui.gradesFlow.grades

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ItemGradeBinding
import com.mezhendosina.sgo.app.utils.setupAsLessonEmoji
import com.mezhendosina.sgo.app.utils.setupGrade
import com.mezhendosina.sgo.app.utils.toGradeType
import com.mezhendosina.sgo.data.netschool.api.grades.entities.GradesItem
import com.mezhendosina.sgo.domain.LessonEmojiUseCase
import javax.inject.Inject
import javax.inject.Singleton

typealias OnGradeClickListener = (GradesItem, View) -> Unit

@Singleton
class GradeAdapter(private val onGradeClickListener: OnGradeClickListener) :
    RecyclerView.Adapter<GradeAdapter.GradeViewHolder>(), View.OnClickListener {
    @Inject
    lateinit var lessonEmojiUseCase: LessonEmojiUseCase

    var grades: List<GradesItem> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class GradeViewHolder(val binding: ItemGradeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onClick(p0: View) {
        val gradeItem = p0.tag as GradesItem
        val view = p0.rootView

        ViewCompat.setTransitionName(
            view,
            p0.context.getString(
                R.string.grade_item_transition_name,
                gradeItem.name,
            ),
        )

        onGradeClickListener(gradeItem, view)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): GradeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemGradeBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return GradeViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: GradeViewHolder,
        position: Int,
    ) {
        val grade = grades[position]
        with(holder.binding) {
            ViewCompat.setTransitionName(
                root,
                holder.itemView.context.getString(
                    R.string.grade_item_transition_name,
                    grade.name,
                ),
            )

            holder.itemView.tag = grade
            lessonEmoji.setupAsLessonEmoji(holder.itemView.context, grade.name)
            lessonName.text = grade.name

            val gradeType = grade.avgGrade().toGradeType()
            this.grade.setupGrade(
                holder.itemView.context,
                gradeType,
                grade.avg ?: "",
            )
        }
    }

    override fun getItemCount(): Int = grades.size
}
