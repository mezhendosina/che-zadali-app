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

package com.mezhendosina.sgo.app.ui.chooseRegion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.ItemRegionBinding
import com.mezhendosina.sgo.app.ui.chooseRegion.entities.ChooseRegionUiEntityItem

typealias OnRegionClickListener = (url: String) -> Unit

class ChooseRegionAdapter(
    private val onRegionClickListener: OnRegionClickListener
) : RecyclerView.Adapter<ChooseRegionAdapter.ChooseRegionViewHolder>(),
    View.OnClickListener {

    var regions: List<ChooseRegionUiEntityItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ChooseRegionViewHolder(val binding: ItemRegionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onClick(v: View) {
        val id = v.tag as ChooseRegionUiEntityItem

        onRegionClickListener.invoke(id.url)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseRegionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRegionBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return ChooseRegionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChooseRegionViewHolder, position: Int) {
        val region = regions[position]
        holder.itemView.tag = region
        holder.binding.schoolName.text = region.name
    }

    override fun getItemCount(): Int = regions.size
}