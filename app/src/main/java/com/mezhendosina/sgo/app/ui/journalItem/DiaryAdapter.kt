package com.mezhendosina.sgo.app.ui.journalItem

import android.os.Trace
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.ItemDiaryBinding
import com.mezhendosina.sgo.app.model.journal.entities.WeekDayUiEntity

class DiaryAdapter(
    private val onHomeworkClickListener: OnHomeworkClickListener
) : RecyclerView.Adapter<DiaryAdapter.ViewHolder>() {

    var weekDays: List<WeekDayUiEntity> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }


    class ViewHolder(
        val binding: ItemDiaryBinding,
        private val onHomeworkClickListener: OnHomeworkClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        val homeworkAdapter = HomeworkAdapter(onHomeworkClickListener)

        val layoutManager = LinearLayoutManager(
            itemView.context,
            LinearLayoutManager.VERTICAL,
            false
        )
        val viewPool = RecyclerView.RecycledViewPool()

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDiaryBinding.inflate(inflater, parent, false)

        return ViewHolder(binding, onHomeworkClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = weekDays[position]
        with(holder.binding) {
            Trace.beginSection("Binding homework")
            this@with.day.text = day.date

            holder.homeworkAdapter.lessons = day.lessons

            homeworkRecyclerView.apply {
                adapter = holder.homeworkAdapter
                layoutManager = holder.layoutManager
            }
            Trace.endSection()
        }
    }

    override fun getItemCount(): Int = weekDays.size
}

