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

package com.mezhendosina.sgo.app.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.ItemAttachmentBinding
import com.mezhendosina.sgo.app.model.answer.FileUiEntity


typealias AttachmentClickListener = (attachment: FileUiEntity, loadingList: MutableList<Int>) -> Unit

class AttachmentAdapter(
    private val actionListener: AttachmentClickListener
) : RecyclerView.Adapter<AttachmentAdapter.AttachmentViewHolder>(),
    View.OnClickListener {

    private class DiffUtilCallback(
        private val oldList: List<Int>,
        private val newList: List<Int>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]

    }


    var attachments: List<FileUiEntity> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var loadingItems = mutableListOf<Int>()
        set(value) {
            val diffUtilCallback = DiffUtilCallback(field, value)
            val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
            field = value
            diffResult.dispatchUpdatesTo(this)

        }

    override fun onClick(v: View) {
        val attachment = v.tag as FileUiEntity
        actionListener.invoke(attachment, loadingItems)
    }

    class AttachmentViewHolder(val binding: ItemAttachmentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAttachmentBinding.inflate(inflater, parent, false)

        binding.deleteAttachment.setOnClickListener(this)
        binding.fileIcon.root.setOnClickListener(this)
        return AttachmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttachmentViewHolder, position: Int) {
        val attachment = attachments[position]
        with(holder.binding) {
            deleteAttachment.visibility = View.GONE
            fileIcon.root.tag = attachment
            fileName.text = attachment.fileName
            if (attachment.id in loadingItems) {
                progressBar.visibility = View.VISIBLE
                fileIcon.root.alpha = 0.5f
            } else {
                progressBar.visibility = View.GONE
                fileIcon.root.alpha = 1f
            }
        }
    }

    override fun getItemCount(): Int = attachments.size
}