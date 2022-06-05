package com.mezhendosina.sgo.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.HomeworkGradeItemBinding
import com.mezhendosina.sgo.data.diary.diary.Mark

class HomeworkGradeAdapter : RecyclerView.Adapter<HomeworkGradeAdapter.HomeworkGradeViewHolder>() {

    var grades: List<Mark> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class HomeworkGradeViewHolder(val binding: HomeworkGradeItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeworkGradeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HomeworkGradeItemBinding.inflate(inflater, parent, false)

        return HomeworkGradeViewHolder(binding)
    }


    override fun onBindViewHolder(holder: HomeworkGradeViewHolder, position: Int) {
        val grade = grades[position]
        with(holder.binding) {
            if (grade.dutyMark) {
                dutyMark.visibility = View.VISIBLE

                goodGrade.visibility = View.INVISIBLE
                badGrade.visibility = View.INVISIBLE
            } else if (grade.mark <= 2) {
                badGrade.visibility = View.VISIBLE

                dutyMark.visibility = View.INVISIBLE
                goodGrade.visibility = View.INVISIBLE

                badGrade.text = grade.mark.toString()
            } else if (grade.mark > 2) {
                goodGrade.visibility = View.VISIBLE

                badGrade.visibility = View.INVISIBLE
                dutyMark.visibility = View.INVISIBLE

                goodGrade.text = grade.mark.toString()
            } else {
                goodGrade.visibility = View.INVISIBLE
                badGrade.visibility = View.INVISIBLE
                dutyMark.visibility = View.INVISIBLE
            }
        }
    }

    override fun getItemCount(): Int = grades.size

}