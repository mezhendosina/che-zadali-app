package com.mezhendosina.sgo.app.ui.itemGrade

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ItemChangeCalculatedGradeBinding

interface ChangeGradeClickListener {

    fun plusGrade(grade: Int)

    fun minusGrade(grade: Int)

    fun manualEditGrade(grade: Int, value: Int)
}

class CalculateGradeAdapter(
    private val changeGradeClickListener: ChangeGradeClickListener
) : RecyclerView.Adapter<CalculateGradeAdapter.ViewHolder>(), View.OnClickListener {

    private class DiffUtilCallback(
        private val oldList: List<Int>,
        private val newList: List<Int>,
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItemPosition == newItemPosition
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]

    }

    var initGrades: List<Int> = emptyList()

    var grades: List<Int> = emptyList()
        set(value) {
            val diffUtilCallback = DiffUtilCallback(field, value)
            val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onClick(v: View) {
        val grade = v.tag as Int
        when (v.id) {
            R.id.plus_grade -> changeGradeClickListener.plusGrade(grade)
            R.id.minus_grade -> changeGradeClickListener.minusGrade(grade)
        }
    }

    class ViewHolder(val binding: ItemChangeCalculatedGradeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChangeCalculatedGradeBinding.inflate(inflater, parent, false)

        binding.plusGrade.setOnClickListener(this)
        binding.minusGrade.setOnClickListener(this)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val grade = grades[position]
        val initGrade = initGrades[position]
        with(holder.binding) {
//            gradeValue.addTextChangedListener {
//                if (it.toString().toIntOrNull() != null) {
//                    changeGradeClickListener.manualEditGrade(
//                        position,
//                        it.toString().toInt()
//                    )
//                }
//            }
            plusGrade.tag = position
            minusGrade.tag = position
            minusGrade.isEnabled = grade - initGrade > 0
            val deltaGrade = grade - initGrade
            when (position) {
                GradeItemFragment.FIVE_GRADE -> {
                    header.text = holder.itemView.context.getText(R.string.five_grade)
                    gradeValue.setText(deltaGrade.toString())
                }
                GradeItemFragment.FOUR_GRADE -> {
                    header.text = holder.itemView.context.getText(R.string.four_grade)
                    gradeValue.setText(deltaGrade.toString())
                }
                GradeItemFragment.THREE_GRADE -> {
                    header.text = holder.itemView.context.getText(R.string.three_grade)
                    gradeValue.setText(deltaGrade.toString())
                }
                GradeItemFragment.TWO_GRADE -> {
                    header.text = holder.itemView.context.getText(R.string.two_grade)
                    gradeValue.setText(deltaGrade.toString())
                }
            }
        }
    }

    override fun getItemCount(): Int = grades.size

}