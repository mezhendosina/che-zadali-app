package com.che.zadali.sgoapp.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.che.zadali.sgoapp.databinding.GradeSumItemBinding

class GradeNumAdapter : RecyclerView.Adapter<GradeNumAdapter.GradeNumViewHolder>() {

    var gradeNumList: List<Any> = emptyList() //TODO изменить тип массива
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class GradeNumViewHolder(
        val binding: GradeSumItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradeNumViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = GradeSumItemBinding.inflate(inflater, parent, false)

        return GradeNumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GradeNumViewHolder, position: Int) {
        val gradeNum = gradeNumList[position]
        with(holder.binding) {
            //TODO привязка элементов к данным
        }
    }

    override fun getItemCount(): Int = gradeNumList.size
}