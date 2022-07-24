package com.mezhendosina.sgo.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.ItemGradeValueBinding
import com.mezhendosina.sgo.app.ui.grades.showBadGrade
import com.mezhendosina.sgo.app.ui.grades.showGoodGrade
import com.mezhendosina.sgo.app.ui.grades.showMidGrade
import com.mezhendosina.sgo.data.layouts.diary.diary.Mark

class HomeworkGradeAdapter : RecyclerView.Adapter<HomeworkGradeAdapter.HomeworkGradeViewHolder>() {

    var grades: List<Mark> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class HomeworkGradeViewHolder(val binding: ItemGradeValueBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeworkGradeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemGradeValueBinding.inflate(inflater, parent, false)

        return HomeworkGradeViewHolder(binding)
    }


    override fun onBindViewHolder(holder: HomeworkGradeViewHolder, position: Int) {
        val grade = grades[position]
        with(holder.binding) {

            if (grade.dutyMark) {
                dutyMark.visibility = View.VISIBLE
            } else when (grade.mark) {
                1, 2 -> {
                    showBadGrade(this)
                    badGrade.text = grade.mark.toString()
                }
                3 -> {
                    showMidGrade(this)
                    midGrade.text = "3"
                }
                4, 5 -> {
                    showGoodGrade(this)
                    goodGrade.text = grade.mark.toString()
                }
            }
        }
    }

    override fun getItemCount(): Int = grades.size
}