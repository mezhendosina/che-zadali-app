package com.che.zadali.sgoapp.data.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.che.zadali.sgo_app.data.diary.Lesson
import com.che.zadali.sgoapp.R.color
import com.che.zadali.sgoapp.databinding.HomeworkItemBinding


class HomeworkAdapter(private val lessonList: List<Lesson>) :
    RecyclerView.Adapter<HomeworkAdapter.HomeworkViewHolder>() {

    var lessons: List<Lesson> = lessonList
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }
    var expand = false

    class HomeworkViewHolder(
        val binding: HomeworkItemBinding
    ) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeworkViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HomeworkItemBinding.inflate(inflater, parent, false)


        binding.root.setOnClickListener {
            expand = !expand
            if (expand) {
                binding.expandMoreHomework.animate().rotation(180f)
                binding.homework.apply {
                    this.visibility = View.VISIBLE
                }
            } else {
                binding.expandMoreHomework.animate().rotation(0f)
                binding.homework.visibility = View.GONE
            }
        }

        return HomeworkViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HomeworkViewHolder,
        position: Int
    ) {
        val lesson = lessons.sortedBy { it.number }[position]
        with(holder.binding) {
            holder.itemView.tag = lesson
            if (lesson.assignments != null) {
                expandMoreHomework.visibility = View.VISIBLE
                homework.text = lesson.assignments[0].assignmentName
                if (lesson.assignments.any { it.mark != null }) {
                    lesson.assignments.forEach {
                        if (it.mark != null) {
                            grade.text = it.mark.mark?.toString()
                            grade.visibility = View.VISIBLE
                            if (it.mark.dutyMark){

                            }
                        }
                        else {
                            grade.visibility = View.GONE
                        }
                    }
                }
            } else {
                expandMoreHomework.visibility = View.GONE
                homework.text = null
            }
            if (lesson.isEaLesson) {
                strangePuzzlePiece.visibility = View.VISIBLE
            } else {
                strangePuzzlePiece.visibility = View.GONE
            }

            lessonName.text = lesson.subjectName
            lessonNumber.text = lesson.number.toString()
            lessonTime.text = "${lesson.startTime} - ${lesson.endTime}"
        }
    }

    override fun getItemCount(): Int = lessons.size


}