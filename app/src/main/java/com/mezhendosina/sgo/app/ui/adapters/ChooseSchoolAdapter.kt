package com.mezhendosina.sgo.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.ItemSchoolBinding
import com.mezhendosina.sgo.app.model.chooseSchool.SchoolUiEntity

typealias OnSchoolClickListener = (SchoolUiEntity) -> Unit

class ChooseSchoolAdapter(private val onSchoolClickListener: OnSchoolClickListener) :
    RecyclerView.Adapter<ChooseSchoolAdapter.ChooseSchoolViewHolder>(),
    View.OnClickListener {

    var schools = emptyList<SchoolUiEntity>()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }


    class ChooseSchoolViewHolder(val binding: ItemSchoolBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onClick(v: View?) {
        val school = v?.tag as SchoolUiEntity
        onSchoolClickListener(school)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseSchoolViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSchoolBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return ChooseSchoolViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChooseSchoolViewHolder, position: Int) {
        val school = schools[position]
        with(holder.binding) {
            holder.itemView.tag = school

            schoolCity.text = school.city
            schoolName.text = school.school
        }
    }

    override fun getItemCount(): Int = schools.size


}
