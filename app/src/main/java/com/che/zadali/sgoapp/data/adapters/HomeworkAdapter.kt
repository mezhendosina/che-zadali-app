package com.che.zadali.sgoapp.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.che.zadali.sgo_app.data.diary.Lesson
import com.che.zadali.sgoapp.databinding.HomeworkItemBinding
import com.che.zadali.sgoapp.ui.screens.mainActivity.HomeworkBottomSheet

class HomeworkAdapter(
    private val lessonList: List<Lesson>,
    private val supportFragmentManager: FragmentManager
) :
    RecyclerView.Adapter<HomeworkAdapter.HomeworkViewHolder>(), View.OnClickListener {

    var lessons: List<Lesson> = lessonList
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class HomeworkViewHolder(
        val binding: HomeworkItemBinding
    ) : RecyclerView.ViewHolder(binding.root)


    override fun onClick(v: View) {
        val homework = v.tag as Lesson
        HomeworkBottomSheet(homework).show(supportFragmentManager, HomeworkBottomSheet.TAG)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeworkViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HomeworkItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
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
                homework.text = lesson.assignments[0].assignmentName
                root.isClickable = true
                if (lesson.assignments.any { it.mark != null }) {
                    lesson.assignments.forEach {
                        if (it.mark != null) {
                            grade.text = it.mark.mark?.toString()
                            grade.visibility = View.VISIBLE
                            if (it.mark.dutyMark) {
//TODO точки
                            }
                        } else {
                            grade.visibility = View.GONE
                        }
                    }
                }
            } else {
                root.isClickable = false
                homework.visibility = View.GONE
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