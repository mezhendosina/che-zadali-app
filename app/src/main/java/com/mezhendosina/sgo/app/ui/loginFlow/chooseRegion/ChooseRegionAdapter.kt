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

package com.mezhendosina.sgo.app.ui.loginFlow.chooseRegion

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ItemRegionBinding
import com.mezhendosina.sgo.app.ui.loginFlow.chooseRegion.entities.ChooseRegionUiEntityItem
import com.mezhendosina.sgo.data.netschool.api.regions.Regions

typealias OnRegionClickListener = (regionName: String) -> Unit

class ChooseRegionAdapter(
    private val onRegionClickListener: OnRegionClickListener
) : RecyclerView.Adapter<ChooseRegionAdapter.ChooseRegionViewHolder>(),
    View.OnClickListener {

    var regions: List<ChooseRegionUiEntityItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var selectedItem = ""

    class ChooseRegionViewHolder(val binding: ItemRegionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onClick(v: View) {
        val id = v.tag as ChooseRegionUiEntityItem
        val oldValue = regions.indexOfFirst { it.name == selectedItem }
        val newValue = regions.indexOfFirst { it.name == id.name }
        selectedItem = id.name
        notifyItemChanged(oldValue)
        notifyItemChanged(newValue)
        onRegionClickListener.invoke(id.name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseRegionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRegionBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return ChooseRegionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChooseRegionViewHolder, position: Int) {
        val region = regions[position]

        val typedValue = TypedValue()
        if (selectedItem == region.name) {
            holder.itemView.context.theme.resolveAttribute(
                com.google.android.material.R.attr.colorOutlineVariant, typedValue, true
            )
            holder.binding.root.setBackgroundColor(typedValue.data)
        } else {
            val transparentColor = holder.itemView.context.getColor(android.R.color.transparent)
            holder.binding.root.setBackgroundColor(transparentColor)
        }
        holder.itemView.tag = region
        holder.binding.schoolName.text = region.name
        val regionEmblem = loadRegionEmblem(holder.itemView.context, region.name)
        Glide.with(holder.itemView.context).load(regionEmblem).into(holder.binding.emblem)
    }

    override fun getItemCount(): Int = regions.size

    private fun loadRegionEmblem(context: Context, region: String): Drawable? {
        val outputDrawable = AppCompatResources.getDrawable(
            context, when (region) {
                Regions.ALTAI_KRAI -> R.drawable.emblem_altai
                Regions.AMUR_OBL -> R.drawable.emblem_amur
                Regions.CHEL_OBL -> R.drawable.emblem_chel
                Regions.CHERNOG -> R.drawable.emblem_chern
                Regions.CHUV_RESP -> R.drawable.emblme_chuvashsk
                Regions.KALUG_OBL -> R.drawable.emblem_kaluga
                Regions.KOSTROMS_OBL -> R.drawable.emblem_kostromsk
                Regions.KRASNOD_KRAI -> R.drawable.emblem_krasnod
                Regions.LENINGRAD_OBL -> R.drawable.emblem_leningradsk
                Regions.PRIMORSK_KRAI -> R.drawable.embem_primork
                Regions.RESP_KOMI -> R.drawable.emblem_resp_komi
                Regions.RESP_MARI_EL -> R.drawable.emblem_resp_mari_el
                Regions.RESP_BUR -> R.drawable.emblem_resp_bur
                Regions.SAHALINSK_OBL -> R.drawable.emblem_sahalin
                Regions.RESP_MORD -> R.drawable.emblem_resp_mord
                Regions.RESP_SAHA -> R.drawable.emblem_resp_saha
                Regions.SAMARSK_OBL -> R.drawable.emblem_samark
                Regions.TVERSK_OBL -> R.drawable.emblem_tverskaya
                Regions.ZABAIKAL_KRAI -> R.drawable.emblem_zbaikalsk
                Regions.YLANOVSK_OBL -> R.drawable.emblem_ylanovsk
                Regions.TOMSK_OBL -> R.drawable.emblem_tomsk
                Regions.YAMAL -> R.drawable.emblem_yamal_nen
                else -> R.drawable.emblem_resp_bur
            }
        )
        return outputDrawable
    }
}

