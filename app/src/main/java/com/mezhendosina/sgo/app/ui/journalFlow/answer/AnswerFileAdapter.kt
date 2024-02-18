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

package com.mezhendosina.sgo.app.ui.journalFlow.answer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ItemAttachmentBinding
import com.mezhendosina.sgo.app.model.answer.FileUiEntity
import javax.inject.Singleton

interface FileActionListener {
    fun onClick(file: FileUiEntity)

    fun deleteFile(attachmentId: Int)

    fun editDescription(attachmentId: Int)
}

@Singleton
class AnswerFileAdapter(
    private val viewModel: AnswerViewModel,
    private val fileActionListener: FileActionListener,
) : RecyclerView.Adapter<AnswerFileAdapter.ViewHolder>(), View.OnClickListener {
    var files: List<FileUiEntity> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(val binding: ItemAttachmentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onClick(v: View) {
        val file = v.tag as FileUiEntity
        when (v.id) {
            R.id.delete_attachment -> {
                val position = files.indexOf(file)
                viewModel.deleteFile(files[position])
                files = files.filter { it.id != file.id }

                notifyItemRemoved(position)
            }

            else -> fileActionListener.onClick(file)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAttachmentBinding.inflate(inflater, parent, false)

        binding.deleteAttachment.setOnClickListener(this)
        binding.fileIcon.root.setOnClickListener(this)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val file = files[position]
        with(holder.binding) {
            fileIcon.root.tag = file
            deleteAttachment.tag = file
            fileName.text = file.fileName
            deleteAttachment.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            // TODO file description
        }
    }

    override fun getItemCount(): Int = files.size
}
