/*
 * Copyright 2023 Eugene Menshenin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mezhendosina.sgo.app.ui.lessonItem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ItemUploadedAttachmentBinding
import com.mezhendosina.sgo.data.requests.sgo.homework.entities.FileUiEntity

typealias OnFileClickListener = (file: FileUiEntity) -> Unit

interface FileActionListener {

    fun deleteFile(attachmentId: Int)

    fun editDescription(attachmentId: Int)

    fun replaceFile(attachmentId: Int)
}


class AnswerFileAdapter(
    private val onFileClickListener: OnFileClickListener,
    private val fileActionListener: FileActionListener
) : RecyclerView.Adapter<AnswerFileAdapter.ViewHolder>(), View.OnClickListener {

    var files: List<FileUiEntity> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(val binding: ItemUploadedAttachmentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onClick(v: View) {
        val file = v.tag as FileUiEntity
        when (v.id) {
            R.id.more -> {
                showMoreMenu(v, file)
            }
            else -> onFileClickListener.invoke(file)
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
            more.tag = file
            fileName.text = file.fileName
            if (!file.description.isNullOrEmpty()) {
                fileDescription.text = file.description
                fileDescription.visibility = View.VISIBLE
            } else fileDescription.visibility = View.GONE
        }
    }


    override fun getItemCount(): Int = files.size

    private fun showMoreMenu(v: View, file: FileUiEntity) {
        val popup = PopupMenu(v.context, v)
//        popup.setForceShowIcon(true)
        val context = v.context
//        popup.menu.add(0, REPLACE_FILE, 0, context.getString(R.string.replace_file)) TODO
        popup.menu.add(0, EDIT_DESCRIPTION, 0, context.getString(R.string.edit_description))
        popup.menu.add(0, DELETE_FILE, 0, context.getString(R.string.delete_file))
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                REPLACE_FILE -> {
                    fileActionListener.replaceFile(file.id)
                }
                EDIT_DESCRIPTION -> {
                    fileActionListener.editDescription(file.id)
                }
                DELETE_FILE -> {
                    fileActionListener.deleteFile(file.id)
                }
            }
            return@setOnMenuItemClickListener true
        }

        popup.show()
    }

    companion object {
        private const val REPLACE_FILE = 0
        private const val EDIT_DESCRIPTION = 1
        private const val DELETE_FILE = 2
    }
}