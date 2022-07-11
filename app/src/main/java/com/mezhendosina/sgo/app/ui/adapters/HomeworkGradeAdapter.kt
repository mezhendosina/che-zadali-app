package com.mezhendosina.sgo.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.ItemHomeworkGradeBinding
import com.mezhendosina.sgo.data.layouts.diary.diary.Mark

class HomeworkGradeAdapter : RecyclerView.Adapter<HomeworkGradeAdapter.HomeworkGradeViewHolder>() {

    var grades: List<Mark> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class HomeworkGradeViewHolder(val binding: ItemHomeworkGradeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeworkGradeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHomeworkGradeBinding.inflate(inflater, parent, false)

        return HomeworkGradeViewHolder(binding)
    }


    override fun onBindViewHolder(holder: HomeworkGradeViewHolder, position: Int) {
        val grade = grades[position]
        with(holder.binding) {
            goodGrade.visibility = View.INVISIBLE
            threeGrade.visibility = View.INVISIBLE
            badGrade.visibility = View.INVISIBLE
            dutyMark.visibility = View.INVISIBLE
            if (grade.dutyMark) {
                dutyMark.visibility = View.VISIBLE
            } else when (grade.mark) {
                1, 2 -> {
                    badGrade.visibility = View.VISIBLE
                    badGrade.text = grade.mark.toString()
                }
                3 -> {
                    threeGrade.visibility = View.VISIBLE
                    threeGrade.text = "3"
                }
                4, 5 -> {
                    goodGrade.visibility = View.VISIBLE
                    goodGrade.text = grade.mark.toString()
                }
            }
        }
    }

    override fun getItemCount(): Int = grades.size
}