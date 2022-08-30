package com.mezhendosina.sgo.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.ItemGradeBinding
import com.mezhendosina.sgo.app.databinding.ItemGradeValueBinding
import com.mezhendosina.sgo.app.ui.grades.showBadGrade
import com.mezhendosina.sgo.app.ui.grades.showGoodGrade
import com.mezhendosina.sgo.app.ui.grades.showMidGrade
import com.mezhendosina.sgo.data.requests.grades.entities.GradesItem

typealias OnGradeClickListener = (GradesItem) -> Unit

class GradeAdapter(private val onGradeClickListener: OnGradeClickListener) :
    RecyclerView.Adapter<GradeAdapter.GradeViewHolder>(), View.OnClickListener {

    var grades: List<GradesItem> = emptyList()
        set(newValue) {
            field = newValue.filter { it.name != "Итого" }
            notifyDataSetChanged()
        }

    class GradeViewHolder(val binding: ItemGradeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onClick(p0: View) {
        val gradeItem = p0.tag as GradesItem
        onGradeClickListener(gradeItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemGradeBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return GradeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GradeViewHolder, position: Int) {
        val grade = grades[position]
        with(holder.binding) {
            holder.itemView.tag = grade
            lessonName.text = grade.name
            bindGradeValue(grade, this.grade)
        }
    }

    override fun getItemCount(): Int = grades.size


}

fun bindGradeValue(grade: GradesItem, binding: ItemGradeValueBinding) {
    binding.apply {
        if (!grade.avg.isNullOrEmpty()) {
            val avgDouble = grade.avg.replace(",", ".").toDouble()
            if (avgDouble < 2.5) {
                badGrade.text = grade.avg
                showBadGrade(this)
            } else if (2.5 <= avgDouble && avgDouble < 3.5) {
                midGrade.text = grade.avg
                showMidGrade(this)
            } else if (avgDouble >= 3.5) {
                goodGrade.text = grade.avg
                showGoodGrade(this)
            }

            when (grade.avg) {
                "5,0" -> goodGrade.text = "5"
                "4,0" -> goodGrade.text = "4"
                "3,0" -> midGrade.text = "3"
                "2,0" -> badGrade.text = "2"
                "1,0" -> badGrade.text = "1"
            }
        }
    }
}