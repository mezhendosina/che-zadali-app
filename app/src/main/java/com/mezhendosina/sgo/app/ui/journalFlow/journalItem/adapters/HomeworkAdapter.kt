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
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ItemHomeworkBinding
import com.mezhendosina.sgo.app.model.journal.entities.LessonUiEntity
import com.mezhendosina.sgo.app.utils.LessonNameFrom
import com.mezhendosina.sgo.app.utils.setup

typealias OnHomeworkClickListener = (LessonUiEntity, View) -> Unit

class HomeworkAdapter(
    private val onHomeworkClickListener: OnHomeworkClickListener
) : RecyclerView.Adapter<HomeworkAdapter.HomeworkViewHolder>(), View.OnClickListener {

    private class DiffUtilCallback(
        private val oldList: List<LessonUiEntity>,
        private val newList: List<LessonUiEntity>,
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].classmeetingId == newList[newItemPosition].classmeetingId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]

    }

    var lessons: List<LessonUiEntity> = emptyList()
        set(value) {
            val diffUtilCallback = DiffUtilCallback(field, value)
            val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
            field = value.sortedBy { it.number }
            diffResult.dispatchUpdatesTo(this)

        }


    class HomeworkViewHolder(val binding: ItemHomeworkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val homeworkAdapter = HomeworkGradeAdapter()

        val layoutManager =
            LinearLayoutManager(
                itemView.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )

        val viewPool = RecyclerView.RecycledViewPool()
    }

    override fun onClick(v: View) {
        val lesson = v.tag as LessonUiEntity
        val view = v.rootView

        ViewCompat.setTransitionName(
            view,
            v.context.getString(
                R.string.lesson_item_transition_name,
                lesson.classmeetingId.toString()
            )
        )

        onHomeworkClickListener(lesson, view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeworkViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHomeworkBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)

        return HomeworkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeworkViewHolder, position: Int) {
        val lesson = lessons[position]
        with(holder.binding) {
            ViewCompat.setTransitionName(
                root,
                holder.itemView.context.getString(
                    R.string.lesson_item_transition_name,
                    lesson.classmeetingId.toString()
                )
            )
            holder.itemView.tag = lesson
            lessonNumber.text = lesson.number.toString()
            lessonName.setup(holder.itemView.context, lesson.subjectName, LessonNameFrom.JOURNAL)
            lessonTime.text = holder.itemView.context.getString(
                R.string.start_end_date,
                lesson.startTime,
                lesson.endTime
            )

            if (lesson.homework != null || lesson.assignments?.find { it.mark != null } != null) {
                if (lesson.homework?.assignmentName?.isNotEmpty() == true) {
                    homework.visibility = View.VISIBLE
                    homework.text = lesson.homework.assignmentName
                } else {
                    homework.visibility = View.GONE
                    homework.text = null
                }
                root.isClickable = true
                root.isFocusable = true
            } else {
                homework.visibility = View.GONE
                root.isClickable = false
                root.isFocusable = false
            }

            val findAttachments =
                lesson.assignments?.find { assignmentUiEntity ->
                    assignmentUiEntity.attachments.isNotEmpty() && assignmentUiEntity.attachments.find { it.attachments.isNotEmpty() } != null
                }
            assignmentTypes.attachment.visibility =
                if (findAttachments != null) {
                    View.VISIBLE
                } else View.GONE


            val findFilesAnswer =
                lesson.assignments?.find { assignmentUiEntity ->
                    assignmentUiEntity.attachments.isNotEmpty() && assignmentUiEntity.attachments.find { it.answerFiles.isNotEmpty() } != null
                }

            assignmentTypes.homeworkAnswer.visibility =
                if (lesson.homework?.textAnswer != null || findFilesAnswer != null) {
                    View.VISIBLE
                } else View.GONE

            if (lesson.assignments?.find { it.mark != null } != null) {
                holder.homeworkAdapter.grades = lesson.assignments.filter { it.mark != null }

                grades.apply {
                    adapter = holder.homeworkAdapter
                    this.layoutManager = holder.layoutManager
                    setRecycledViewPool(holder.viewPool)
                }
            } else {
                grades.adapter = null
            }
        }
    }


    override fun getItemCount(): Int = lessons.size

}
