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

package com.mezhendosina.sgo.app.ui.journalFlow.journalItem.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.ItemPastMandatoryBinding
import com.mezhendosina.sgo.data.DateManipulation
import com.mezhendosina.sgo.data.netschool.api.diary.entities.PastMandatoryEntity
import dagger.Module
import javax.inject.Singleton

typealias PastMandatoryClickListener = (PastMandatoryEntity) -> Unit

@Singleton
class PastMandatoryAdapter(
    private val pastMandatoryClickListener: PastMandatoryClickListener
) : RecyclerView.Adapter<PastMandatoryAdapter.PastMandatoryViewHolder>(),
    View.OnClickListener {

    var items: List<PastMandatoryEntity> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class PastMandatoryViewHolder(val binding: ItemPastMandatoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onClick(v: View) {
        val pastMandatoryItem = v.tag as PastMandatoryEntity

        pastMandatoryClickListener.invoke(pastMandatoryItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PastMandatoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPastMandatoryBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return PastMandatoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PastMandatoryViewHolder, position: Int) {
        val item = items[position]
        holder.itemView.tag = item
        with(holder.binding) {
            this.dueDate.text = DateManipulation(item.dueDate).dateFormatter()
            this.homework.text = item.assignmentName
            this.lessonName.text = item.subjectName
        }
    }

    override fun getItemCount(): Int = items.size
}