package com.mezhendosina.sgo.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.AttachmentItemBinding
import com.mezhendosina.sgo.app.ui.hideAnimation
import com.mezhendosina.sgo.app.ui.more.MoreViewModel
import com.mezhendosina.sgo.app.ui.showAnimation
import com.mezhendosina.sgo.data.attachments.Attachment

interface AttachmentClickListener {
    fun onClick(attachment: Attachment)
}

class AttachmentAdapter(
    private val viewModel: MoreViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val actionListener: AttachmentClickListener
) :
    RecyclerView.Adapter<AttachmentAdapter.AttachmentViewHolder>(),
    View.OnClickListener {
    private lateinit var binding: AttachmentItemBinding
    var attachments: List<Attachment> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onClick(v: View) {
        val attachment = v.tag as Attachment

        hideAnimation(binding.fileIcon)
        showAnimation(binding.progressBar)

        actionListener.onClick(attachment)
    }

    class AttachmentViewHolder(val binding: AttachmentItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = AttachmentItemBinding.inflate(inflater, parent, false)

        viewModel.loading.observe(lifecycleOwner) {
            binding.progressBar.progress = it
            if (it == 100) {
                hideAnimation(binding.progressBar)
                showAnimation(binding.fileIcon)
            }
        }
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