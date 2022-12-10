package com.mezhendosina.sgo.app.ui.chooseYearBottomSheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.ItemChooseYearBinding
import com.mezhendosina.sgo.data.requests.sgo.settings.entities.YearListResponseEntity

typealias OnYearClickListener = (year: YearListResponseEntity) -> Unit

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