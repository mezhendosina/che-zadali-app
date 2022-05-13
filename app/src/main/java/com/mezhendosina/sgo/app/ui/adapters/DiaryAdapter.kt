package com.mezhendosina.sgo.app.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.DiaryItemBinding
import com.mezhendosina.sgo.data.attachments.AttachmentsResponseItem
import com.mezhendosina.sgo.data.diary.diary.WeekDay
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun dateToRussian(date: String): String {
    val a = SimpleDateFormat("yyyy-MM-dd'T'00:00:00").parse(date)
    val locale = Locale("ru", "RU")

    return SimpleDateFormat("EEEE, dd MMMM", locale).format(a!!).replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()
        ) else it.toString()
    }

}

class DiaryAdapter(private val onHomeworkClickListener: OnHomeworkClickListener) :
    RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>() {

    var diary: List<WeekDay> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    var attachments: List<AttachmentsResponseItem> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }
    private val viewPool = RecyclerView.RecycledViewPool()

    class DiaryViewHolder(val binding: DiaryItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DiaryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DiaryItemBinding.inflate(inflater, parent, false)

        return DiaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        val day = diary[position]
        with(holder.binding) {
            this.day.text = dateToRussian(day.date)
            val layoutManager =
                LinearLayoutManager(holder.itemView.context, LinearLayoutManager.VERTICAL, false)
            val homeworkAdapter = HomeworkAdapter(onHomeworkClickListener)

            homeworkAdapter.lessons = day.lessons
            homeworkAdapter.attachments = attachments

            homeworkRecyclerView.apply {
                adapter = homeworkAdapter
                this.layoutManager = layoutManager
                setRecycledViewPool(viewPool)
            }
        }
    }

    override fun getItemCount(): Int = diary.size
}