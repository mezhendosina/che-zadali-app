package com.mezhendosina.sgo.app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.GradeItemBinding
import com.mezhendosina.sgo.data.grades.GradeItem

class GradeAdapter : RecyclerView.Adapter<GradeAdapter.GradeViewHolder>() {

    var grades: List<GradeItem> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class GradeViewHolder(val binding: GradeItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = GradeItemBinding.inflate(inflater, parent, false)

        return GradeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GradeViewHolder, position: Int) {
        val grade = grades[position]
        with(holder.binding) {
            lessonName.text = grade.lesson_name
            this.grade.text = grade.grade.toString()
        }
    }

    override fun getItemCount(): Int = grades.size
}