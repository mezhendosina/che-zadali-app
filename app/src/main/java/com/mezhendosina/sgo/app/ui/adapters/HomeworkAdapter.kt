package com.mezhendosina.sgo.app.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.HomeworkItemBinding
import com.mezhendosina.sgo.data.diary.diary.Lesson
import java.text.SimpleDateFormat
import java.util.*



class HomeworkAdapter :
    RecyclerView.Adapter<HomeworkAdapter.HomeworkViewHolder>() {

    var lessons: List<Lesson> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class HomeworkViewHolder(val binding: HomeworkItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeworkViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = HomeworkItemBinding.inflate(inflater, parent, false)

        return HomeworkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeworkViewHolder, position: Int) {
        val lesson = lessons[position]
        with(holder.binding) {

            lessonName.text = lesson.subjectName
            lessonNumber.text = lesson.number.toString()
            lessonTime.text = "${lesson.startTime}-${lesson.endTime}"

            if (!lesson.assignments.isNullOrEmpty()) {
                var grade = ""

                lesson.assignments.forEach { assign ->
                    if (assign.mark != null) {
                        grade += "${assign.mark.mark}  "
                    }
                    if (assign.typeId == 3) {
                        homework.text = assign.assignmentName
                        homework.visibility = View.VISIBLE
                    } else {
                        homework.visibility = View.GONE
                    }
                }

                grades.text = grade
            }
        }
    }

    override fun getItemCount(): Int = lessons.size
}