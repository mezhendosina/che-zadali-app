package com.che.zadali.sgoapp.data.adapters

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.che.zadali.sgoapp.data.layout.messageList.Record
import com.che.zadali.sgoapp.data.newLineDate
import com.che.zadali.sgoapp.databinding.MessagesItemBinding

interface MessagesActionListener {
    fun onClick(message: Record)
}

class MessagesAdapter(private val actionListener: MessagesActionListener) :
    RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder>(),
    View.OnClickListener {

    var messages: List<Record> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class MessagesViewHolder(
        val binding: MessagesItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onClick(v: View) {
        val message = v.tag as Record
        actionListener.onClick(message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MessagesItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return MessagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
        val message = messages[position]
        with(holder.binding) {
            holder.itemView.tag = message
            if (message.read == "Y") {
                newMessage.visibility = View.GONE
                author.typeface = Typeface.DEFAULT
                header.typeface = Typeface.DEFAULT
            } else {
                newMessage.visibility = View.VISIBLE
                author.typeface = Typeface.DEFAULT_BOLD
                header.typeface = Typeface.DEFAULT_BOLD
            }
            author.text = message.subj
            header.text = message.fromName
            date.text = newLineDate(message.sent)
        }
    }

    override fun getItemCount(): Int = messages.size
}