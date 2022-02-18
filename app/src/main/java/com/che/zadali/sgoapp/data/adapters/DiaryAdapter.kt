package com.che.zadali.sgoapp.data.adapters

import android.animation.LayoutTransition
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.che.zadali.sgo_app.data.diary.WeekDay
import com.che.zadali.sgoapp.data.dateToRussian
import com.che.zadali.sgoapp.databinding.DiaryItemBinding

class DiaryAdapter() :
    RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>() {

    var diary: List<WeekDay> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }
    private val viewPool = RecyclerView.RecycledViewPool()

    class DiaryViewHolder(
        val binding: DiaryItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DiaryItemBinding.inflate(inflater, parent, false)
        return DiaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        val weekDay = diary[position]
        val layoutManager =
            LinearLayoutManager(holder.itemView.context, LinearLayoutManager.VERTICAL, false)
        with(holder.binding) {
            root.layoutTransition.enableTransitionType(LayoutTransition.CHANGE_DISAPPEARING)
            lessonsRecyclerView.apply {
                adapter = HomeworkAdapter(weekDay.lessons)
                this.layoutManager = layoutManager
                setRecycledViewPool(viewPool)
            }
            header.text = dateToRussian(weekDay.date, true)
        }
    }

    override fun getItemCount(): Int = diary.size
}