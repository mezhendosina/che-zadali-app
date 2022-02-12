package com.che.zadali.sgoapp.data.diary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.che.zadali.sgo_app.data.diary.Lesson
import com.che.zadali.sgoapp.databinding.HomeworkItemBinding

class TodayHomeworkAdapter() :
    RecyclerView.Adapter<TodayHomeworkAdapter.TodayHomeworkViewHolder>() {

    var lessons: List<Lesson> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class TodayHomeworkViewHolder(
        val binding: HomeworkItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayHomeworkViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HomeworkItemBinding.inflate(inflater, parent, false)

        return TodayHomeworkViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: TodayHomeworkViewHolder,
        position: Int
    ) {
        val lesson = lessons[position]
        with(holder.binding) {
            if (lesson.assignments != null) {
                expandMoreHomework.visibility = View.VISIBLE
                homework.text = lesson.assignments[0].assignmentName
            } else {
                expandMoreHomework.visibility = View.GONE
                homework.text = null
            }
            lessonName.text = lesson.subjectName
            lessonNumber.text = lesson.number.toString()
            lessonTime.text = "${lesson.startTime} - ${lesson.endTime}"
        }
    }

    override fun getItemCount(): Int = lessons.size

}