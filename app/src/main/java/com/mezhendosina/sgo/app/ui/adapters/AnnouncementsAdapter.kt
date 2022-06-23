package com.mezhendosina.sgo.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.AnnouncementsItemBinding
import com.mezhendosina.sgo.data.layouts.announcements.AnnouncementsResponseItem
import io.noties.markwon.Markwon

typealias OnAnnouncementClickListener = (AnnouncementsResponseItem) -> Unit

class AnnouncementsAdapter(
    private val onAnnouncementClickListener: OnAnnouncementClickListener,
) :
    RecyclerView.Adapter<AnnouncementsAdapter.AnnouncementsViewHolder>(),
    View.OnClickListener {

    var announcements: List<AnnouncementsResponseItem> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }


    class AnnouncementsViewHolder(val binding: AnnouncementsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onClick(p0: View) {
        val announcement = p0.tag as AnnouncementsResponseItem
        onAnnouncementClickListener(announcement)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AnnouncementsItemBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return AnnouncementsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnnouncementsViewHolder, position: Int) {
        val announcement = announcements[position]
        with(holder.binding) {
            holder.itemView.tag = announcement
            announcementHeader.text = announcement.name.parseAsHtml()
            announcementBody.text = announcement.description.parseAsHtml()
        }
    }

    override fun getItemCount(): Int = announcements.size
}
