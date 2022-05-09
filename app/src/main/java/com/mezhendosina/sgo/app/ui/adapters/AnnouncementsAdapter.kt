package com.mezhendosina.sgo.app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.AnnouncementsItemBinding
import com.mezhendosina.sgo.data.announcements.AnnouncementsResponseItem

class AnnouncementsAdapter : RecyclerView.Adapter<AnnouncementsAdapter.AnnouncementsViewHolder>() {

    var announcements: List<AnnouncementsResponseItem> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class AnnouncementsViewHolder(val binding: AnnouncementsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AnnouncementsItemBinding.inflate(inflater, parent, false)
        return AnnouncementsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnnouncementsViewHolder, position: Int) {
        val announcement = announcements[position]
        with(holder.binding) {
            announcementHeader.text = announcement.name
            announcementBody.text = announcement.description
        }
    }

    override fun getItemCount(): Int = announcements.size
}
