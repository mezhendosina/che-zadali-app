package com.mezhendosina.sgo.app.ui.journalItem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.ItemHomeworkBinding
import com.mezhendosina.sgo.app.model.journal.entities.LessonAdapter
import com.mezhendosina.sgo.app.ui.adapters.HomeworkGradeAdapter
import com.mezhendosina.sgo.app.ui.journal.JournalPagerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

typealias OnHomeworkClickListener = (LessonAdapter) -> Unit

class HomeworkAdapter(
    private val onHomeworkClickListener: OnHomeworkClickListener
) : RecyclerView.Adapter<HomeworkAdapter.HomeworkViewHolder>(), View.OnClickListener {

    var lessons: List<LessonAdapter> = emptyList()
        set(value) {
            field = value.sortedBy { it.number }
            notifyDataSetChanged()
        }


    class HomeworkViewHolder(val binding: ItemHomeworkBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onClick(v: View) {
        val lesson = v.tag as LessonAdapter
        onHomeworkClickListener(lesson)
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
            CoroutineScope(Dispatchers.Main).launch {

                holder.itemView.tag = lesson
                lessonNumber.text = lesson.number.toString()
                lessonName.text = lesson.subjectName
                lessonTime.text = "${lesson.startTime} - ${lesson.endTime}"

                if (lesson.homework != null) {
                    homework.text = lesson.homework.assignmentName
                    homework.visibility = View.VISIBLE
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

                if (lesson.assignments?.isNotEmpty() == true) {
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
                        setRecycledViewPool(JournalPagerAdapter.viewPool)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = lessons.size
}
