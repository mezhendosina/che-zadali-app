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