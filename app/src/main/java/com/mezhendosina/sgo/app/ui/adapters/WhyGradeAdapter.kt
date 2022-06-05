package com.mezhendosina.sgo.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.WhyGradeItemBinding
import com.mezhendosina.sgo.data.grades.WhyGradeItem
import com.mezhendosina.sgo.data.homeworkTypes.TypesResponseItem

class WhyGradeAdapter : RecyclerView.Adapter<WhyGradeAdapter.WhyGradeViewHolder>() {

    var grades: List<WhyGradeItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var types: List<TypesResponseItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class WhyGradeViewHolder(val binding: WhyGradeItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WhyGradeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = WhyGradeItemBinding.inflate(inflater, parent, false)

        return WhyGradeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WhyGradeViewHolder, position: Int) {
        val grade = grades[position]
        with(holder.binding) {
            if (grade.mark.dutyMark) {
                dutyMark.visibility = View.VISIBLE

                this.goodGrade.visibility = View.INVISIBLE
                this.badGrade.visibility = View.INVISIBLE
            } else {
                dutyMark.visibility = View.INVISIBLE

                if (grade.mark.mark <= 2) {
                    this.badGrade.visibility = View.VISIBLE
                    this.goodGrade.visibility = View.INVISIBLE

                    this.badGrade.text = grade.mark.mark.toString()
                } else {
                    this.goodGrade.visibility = View.VISIBLE
                    this.badGrade.visibility = View.INVISIBLE

                    this.goodGrade.text = grade.mark.mark.toString()
                }
            }
            this.gradeText.text = grade.assignmentName
            this.gradeType.text = types.find { it.id == grade.typeId }?.name
        }
    }

    override fun getItemCount(): Int = grades.size
}