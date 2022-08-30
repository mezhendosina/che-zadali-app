package com.mezhendosina.sgo.app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.ItemPastMandatoryBinding
import com.mezhendosina.sgo.data.DateManipulation
import com.mezhendosina.sgo.data.requests.diary.entities.PastMandatoryEntity

class PastMandatoryAdapter : RecyclerView.Adapter<PastMandatoryAdapter.PastMandatoryViewHolder>() {

    var items: List<PastMandatoryEntity> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class PastMandatoryViewHolder(val binding: ItemPastMandatoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PastMandatoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPastMandatoryBinding.inflate(inflater, parent, false)
        return PastMandatoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PastMandatoryViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            this.dueDate.text = DateManipulation(item.dueDate).dateFormatter()
            this.homework.text = item.assignmentName
            this.lessonName.text = item.subjectName
        }
    }

    override fun getItemCount(): Int = items.size
}