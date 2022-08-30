package com.mezhendosina.sgo.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.ItemHomeworkBinding
import com.mezhendosina.sgo.app.model.journal.entities.LessonAdapter
import com.mezhendosina.sgo.data.requests.homework.entities.AttachmentsResponseEntity
import com.mezhendosina.sgo.data.requests.diary.entities.Lesson
import com.mezhendosina.sgo.data.requests.diary.entities.Mark

typealias OnHomeworkClickListener = (Lesson) -> Unit

class HomeworkAdapter(private val onHomeworkClickListener: OnHomeworkClickListener) :
    RecyclerView.Adapter<HomeworkAdapter.HomeworkViewHolder>(), View.OnClickListener {

    var lessons: List<LessonAdapter> = emptyList()
        set(newValue) {
            field = newValue.sortedBy { it.number }
            notifyDataSetChanged()
        }

    private val viewPool = RecyclerView.RecycledViewPool()

    class HomeworkViewHolder(val binding: ItemHomeworkBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onClick(v: View) {
        val lesson = v.tag as Lesson
        onHomeworkClickListener(lesson)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeworkViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHomeworkBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return HomeworkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeworkViewHolder, position: Int) {
        val lesson = lessons[position]
        with(holder.binding) {
            holder.itemView.tag = lesson

            lessonName.text = lesson.subjectName
            lessonNumber.text = lesson.number.toString()
            lessonTime.text = "${lesson.startTime}-${lesson.endTime}"

            if (!lesson.assignments.isNullOrEmpty()) {
                var withHomework = false

//                lesson.assignments.forEach { assign ->
//                    if (assign.typeId == 3) {
//                        homework.text = assign.assignmentName
//                        withHomework = true
//                    }
//
//                    if (assign.textAnswer != null)
//                        assignmentTypes.homeworkAnswer.visibility = View.VISIBLE
//

//                    if (attachments.find { assign.id == it.assignmentId } != null)
//                        assignmentTypes.attachment.visibility = View.VISIBLE
//                }

                if (withHomework) homework.visibility = View.VISIBLE
                else homework.visibility = View.GONE

                if (lesson.assignments.isNotEmpty()) {
                    val layoutManager =
                        LinearLayoutManager(
                            holder.itemView.context,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                    val homeworkAdapter = HomeworkGradeAdapter()
                    homeworkAdapter.grades = lesson.assignments

                    grades.apply {
                        adapter = homeworkAdapter
                        this.layoutManager = layoutManager
                        setRecycledViewPool(viewPool)
                    }
                }
            } else {
                grades.visibility = View.GONE
                homework.visibility = View.GONE
                assignmentTypes.root.visibility = View.GONE
                this.root.isClickable = false
            }
        }
    }

    override fun getItemCount(): Int = lessons.size
}
