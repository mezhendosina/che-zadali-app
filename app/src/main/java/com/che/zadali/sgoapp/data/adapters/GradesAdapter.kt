package com.che.zadali.sgoapp.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.che.zadali.sgoapp.databinding.GradeItemBinding

class GradesAdapter : RecyclerView.Adapter<GradesAdapter.GradesViewHolder>() {

    var grades: List<Any> = emptyList() //TODO изменить тип массива
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }
    private val viewPool = RecyclerView.RecycledViewPool()

    class GradesViewHolder(
        val binding: GradeItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = GradeItemBinding.inflate(inflater, parent, false)

        return GradesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GradesViewHolder, position: Int) {
        val grade = grades[position]
        val layoutManager =
            LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
        with(holder.binding) {
            //TODO привязка элементов к данным
            gradesNumber.apply {
                adapter = GradeNumAdapter()
                this.layoutManager = layoutManager
                setRecycledViewPool(viewPool)
            }
            lesson.text = "Литература"
            this.grade.text = "3,64"
        }
    }

    override fun getItemCount() = grades.size

}