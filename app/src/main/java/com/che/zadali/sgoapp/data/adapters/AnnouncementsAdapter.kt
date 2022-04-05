package com.che.zadali.sgoapp.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.che.zadali.sgoapp.data.layout.announcements.AnnouncementsDataItem
import com.che.zadali.sgoapp.data.layout.announcements.Attachment
import com.che.zadali.sgoapp.databinding.AnnouncemetsItemBinding
import com.che.zadali.sgoapp.databinding.AttachmentsItemBinding

interface AnnouncementsActionListener {
    fun onClick(announcementsDataItem: AnnouncementsDataItem)
}

class AnnouncementsAdapter(
    announcementsList: List<AnnouncementsDataItem>,
    private val actionListener: AnnouncementsActionListener
) :
    RecyclerView.Adapter<AnnouncementsAdapter.AnnouncementsViewHolder>(), View.OnClickListener {

    var announcements: List<AnnouncementsDataItem> = announcementsList
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class AnnouncementsViewHolder(
        val binding: AnnouncemetsItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onClick(v: View) {
        val announcement = v.tag as AnnouncementsDataItem
        actionListener.onClick(announcement)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AnnouncemetsItemBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return AnnouncementsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnnouncementsViewHolder, position: Int) {
        val announcement = announcements[position]
        with(holder.binding) {
            holder.itemView.tag = announcement
            this.announcement.text = announcement.description
            this.announcementHeader.text = announcement.name

        }
    }

    override fun getItemCount(): Int = announcements.size
}

class AnnouncementsFilesAdapter(
    files: List<Attachment>,
) :
    RecyclerView.Adapter<AnnouncementsFilesAdapter.AnnouncementsFilesViewHolder>(),
    View.OnClickListener {

    var files: List<Attachment> = files
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class AnnouncementsFilesViewHolder(
        val binding: AttachmentsItemBinding
    ) : RecyclerView.ViewHolder(binding.root)


    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnnouncementsFilesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AttachmentsItemBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return AnnouncementsFilesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnnouncementsFilesViewHolder, position: Int) {
        val file = files[position]
        with(holder.binding) {
            fileName.text = file.name
        }
    }

    override fun getItemCount(): Int = files.size
}