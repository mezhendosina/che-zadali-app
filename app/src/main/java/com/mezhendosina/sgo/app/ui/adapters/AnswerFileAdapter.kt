package com.mezhendosina.sgo.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ItemUploadedAttachmentBinding
import com.mezhendosina.sgo.data.requests.homework.entities.File

typealias OnFileClickListener = (file: File) -> Unit

interface FileActionListener {

    fun deleteFile(attachmentId: Int)

    fun editDescription(attachmentId: Int)

    fun replaceFile(attachmentId: Int)
}


class AnswerFileAdapter(
    private val onFileClickListener: OnFileClickListener,
    private val fileActionListener: FileActionListener
) : RecyclerView.Adapter<AnswerFileAdapter.ViewHolder>(), View.OnClickListener {

    var files: List<File> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(val binding: ItemUploadedAttachmentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onClick(v: View) {
        val file = v.tag as File?
        when (v.id) {
            R.id.more -> {
                println()
                showMoreMenu(v)
            }
            else -> onFileClickListener.invoke(file!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUploadedAttachmentBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.more.setOnClickListener(this)

        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val file = files[position]
        with(holder.binding) {
            holder.itemView.tag = file
            fileName.text = file.fileName
            if (!file.description.isNullOrEmpty()) {
                fileDescription.text = file.description
                fileDescription.visibility = View.VISIBLE
            } else fileDescription.visibility = View.GONE
        }
    }


    override fun getItemCount(): Int = files.size

    private fun showMoreMenu(v: View) {
        val popup = PopupMenu(v.context, v)
//        popup.setForceShowIcon(true)
        popup.setOnMenuItemClickListener {
            val file = v.tag as File
            when (it.itemId) {
                1 -> {
                    fileActionListener.replaceFile(file.id)
                    true
                }
                2 -> {
                    fileActionListener.editDescription(file.id)
                    true
                }
                3 -> {
                    fileActionListener.deleteFile(file.id)
                    true
                }
                else -> true
            }
        }
        popup.menuInflater.inflate(R.menu.menu_upload_attachment, popup.menu)
        popup.show()
    }

    companion object {
        private const val REPLACE_FILE = 0
        private const val EDIT_DESCRIPTION = 1
        private const val DELETE_FILE = 2
    }
}