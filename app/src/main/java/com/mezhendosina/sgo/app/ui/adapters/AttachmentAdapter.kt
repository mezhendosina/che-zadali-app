package com.mezhendosina.sgo.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.ItemAttachmentBinding
import com.mezhendosina.sgo.app.ui.hideAnimation
import com.mezhendosina.sgo.app.ui.showAnimation
import com.mezhendosina.sgo.data.requests.homework.entities.Attachment

interface AttachmentClickListener {
    fun onClick(attachment: Attachment, binding: ItemAttachmentBinding)
}

class AttachmentAdapter(
    private val actionListener: AttachmentClickListener
) :
    RecyclerView.Adapter<AttachmentAdapter.AttachmentViewHolder>(),
    View.OnClickListener {
    private lateinit var binding: ItemAttachmentBinding
    var attachments: List<Attachment> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onClick(v: View) {
        val attachment = v.tag as Attachment

        showAnimation(binding.progressBar)

        actionListener.onClick(attachment, binding)

    }

    class AttachmentViewHolder(val binding: ItemAttachmentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemAttachmentBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return AttachmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttachmentViewHolder, position: Int) {
        val attachment = attachments[position]
        with(holder.binding) {
            holder.itemView.tag = attachment
            this.fileName.text = attachment.originalFileName
        }
    }

    override fun getItemCount(): Int = attachments.size
}