package com.mezhendosina.sgo.app.ui.journalItem

import android.annotation.SuppressLint
import android.os.Trace
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.ItemDiaryBinding
import com.mezhendosina.sgo.app.model.journal.entities.AdapterWeekDay
import com.mezhendosina.sgo.app.model.journal.entities.LessonAdapter
import com.mezhendosina.sgo.app.ui.journal.JournalPagerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DiaryAdapter(
    private val onHomeworkClickListener: OnHomeworkClickListener
) : RecyclerView.Adapter<DiaryAdapter.ViewHolder>() {

    var weekDays: List<AdapterWeekDay> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }


    class ViewHolder(val binding: ItemDiaryBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDiaryBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = weekDays[position]
        with(holder.binding) {
            Trace.beginSection("Binding homework")
            CoroutineScope(Dispatchers.Main).launch {
                this@with.day.text = day.date
                val layoutManager =
                    LinearLayoutManager(holder.itemView.context, LinearLayoutManager.VERTICAL, false)
                val homeworkAdapter = HomeworkAdapter(onHomeworkClickListener)
                homeworkAdapter.lessons = day.lessons

                homeworkRecyclerView.apply {
                    adapter = homeworkAdapter
                    this.layoutManager = layoutManager
                    setRecycledViewPool(viewPool)
                }
            }
            Trace.endSection()
        }
    }

    override fun getItemCount(): Int = weekDays.size
}

