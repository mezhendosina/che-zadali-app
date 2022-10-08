package com.mezhendosina.sgo.app.ui.journalItem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ItemHomeworkBinding
import com.mezhendosina.sgo.app.model.journal.entities.LessonUiEntity
import com.mezhendosina.sgo.app.ui.adapters.HomeworkGradeAdapter

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
        val view = v.rootView.findViewById<ConstraintLayout>(R.id.homework_item)

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
            holder.itemView.tag = lesson
            lessonNumber.text = lesson.number.toString()
            lessonName.text = lesson.subjectName
            lessonTime.text = "${lesson.startTime} - ${lesson.endTime}"

            if (lesson.homework != null) {
                if (lesson.homework.assignmentName.isNotEmpty()) {
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

            assignmentTypes.attachment.visibility =
                if (lesson.assignments?.find { it.attachments.isNotEmpty() } != null) {
                    View.VISIBLE
                } else View.GONE

            assignmentTypes.homeworkAnswer.visibility =
                if (lesson.homework?.textAnswer != null) {
                    View.VISIBLE
                } else View.GONE

            if (lesson.assignments?.find { it.mark != null } != null) {
                holder.homeworkAdapter.grades = lesson.assignments

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
