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

package com.mezhendosina.sgo.app.ui.chooseYearBottomSheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.ItemChooseYearBinding
import com.mezhendosina.sgo.data.netschool.api.settings.entities.YearListResponseEntity
import dagger.Module
import javax.inject.Singleton

typealias OnYearClickListener = (year: YearListResponseEntity) -> Unit

@Singleton
class ChooseYearAdapter(
    private val onYearClickListener: OnYearClickListener
) : RecyclerView.Adapter<ChooseYearAdapter.ChooseYearViewHolder>(),
    View.OnClickListener {

    var years: List<YearListResponseEntity> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ChooseYearViewHolder(val binding: ItemChooseYearBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onClick(v: View) {
        val year = v.tag as YearListResponseEntity

        onYearClickListener(year)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseYearViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChooseYearBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return ChooseYearViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChooseYearViewHolder, position: Int) {
        val year = years[position]

        with(holder.binding) {
            holder.itemView.tag = year
            this.year.text = year.name
        }
    }

    override fun getItemCount(): Int = years.size
}