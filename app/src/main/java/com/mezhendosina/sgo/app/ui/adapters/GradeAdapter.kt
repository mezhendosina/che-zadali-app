package com.mezhendosina.sgo.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.ItemDiaryBinding
import com.mezhendosina.sgo.app.databinding.ItemGradeBinding
import com.mezhendosina.sgo.data.layouts.grades.GradesItem

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

            if (grade.avg != null) {
                if (grade.avg < 2.5) {
                    badGrade.text = grade.avg.toString()
                    showBadGrade(this)
                } else if (2.5 <= grade.avg && grade.avg < 3.5) {
                    midGrade.text = grade.avg.toString()
                    showMidGrade(this)
                } else if (grade.avg >= 3.5) {
                    goodGrade.text = grade.avg.toString()
                    showGoodGrade(this)
                }

                when (grade.avg) {
                    5.0 -> goodGrade.text = "5"
                    4.0 -> goodGrade.text = "4"
                    3.0 -> midGrade.text = "3"
                    2.0 -> badGrade.text = "2"
                    1.0 -> badGrade.text = "1"
                }
            }

//            with(countGrade) {
//                if (grade.one == null) {
//                    oneH.visibility = View.GONE
//                    oneValue.visibility = View.GONE
//                } else {
//                    oneH.visibility = View.VISIBLE
//                    oneValue.visibility = View.VISIBLE
//                    oneValue.text = grade.one.toString()
//                }
//
//                if (grade.two == null) {
//                    twoH.visibility = View.GONE
//                    twoValue.visibility = View.GONE
//                } else {
//                    twoH.visibility = View.VISIBLE
//                    twoValue.visibility = View.VISIBLE
//                    twoValue.text = grade.two.toString()
//                }
//
//                if (grade.three == null) {
//                    threeH.visibility = View.GONE
//                    threeValue.visibility = View.GONE
//                } else {
//                    threeH.visibility = View.VISIBLE
//                    threeValue.visibility = View.VISIBLE
//                    threeValue.text = grade.three.toString()
//                }
//
//                if (grade.four == null) {
//                    fourH.visibility = View.GONE
//                    fourValue.visibility = View.GONE
//                } else {
//                    fourH.visibility = View.VISIBLE
//                    fourValue.visibility = View.VISIBLE
//                    fourValue.text = grade.four.toString()
//                }
//
//                if (grade.five == null) {
//                    fiveH.visibility = View.GONE
//                    fiveValue.visibility = View.GONE
//                } else {
//                    fiveH.visibility = View.VISIBLE
//                    fiveValue.visibility = View.VISIBLE
//                    fiveValue.text = grade.five.toString()
//                }
//            }
        }
    }

    override fun getItemCount(): Int = grades.size

    private fun showBadGrade(binding: ItemGradeBinding) {
        binding.apply {
            midGrade.visibility = View.GONE
            goodGrade.visibility = View.GONE
            goodGrade.text = ""
            badGrade.visibility = View.VISIBLE
        }
    }

    private fun showMidGrade(binding: ItemGradeBinding) {
        binding.apply {
            goodGrade.text = ""
            goodGrade.visibility = View.GONE
            badGrade.visibility = View.GONE
            midGrade.visibility = View.VISIBLE
        }
    }

    private fun showGoodGrade(binding: ItemGradeBinding) {
        binding.apply {
            goodGrade.visibility = View.VISIBLE
            midGrade.visibility = View.GONE
            badGrade.visibility = View.GONE
        }
    }
}