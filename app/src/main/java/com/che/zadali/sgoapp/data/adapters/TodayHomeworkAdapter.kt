package com.che.zadali.sgoapp.data.adapters

import android.animation.LayoutTransition
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
    var expand = false

    class TodayHomeworkViewHolder(
        val binding: HomeworkItemBinding
    ) : RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayHomeworkViewHolder {
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

        return TodayHomeworkViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: TodayHomeworkViewHolder,
        position: Int
    ) {
        val lesson = lessons[position]
        with(holder.binding) {
            holder.itemView.tag = lesson
            root.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
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