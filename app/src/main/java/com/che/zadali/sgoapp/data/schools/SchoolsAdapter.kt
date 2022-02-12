package com.che.zadali.sgoapp.data.schools

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.che.zadali.sgo_app.data.schools.SchoolItem
import com.che.zadali.sgoapp.databinding.SchoolItemBinding

interface SchoolActionListener {
    fun onClick(schoolItem: SchoolItem)
}

class SchoolsAdapter(private val actionListener: SchoolActionListener) :
    RecyclerView.Adapter<SchoolsAdapter.SchoolsViewHolder>(), View.OnClickListener {
    var schools: List<SchoolItem> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class SchoolsViewHolder(
        val binding: SchoolItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onClick(v: View) {
        val school = v.tag as SchoolItem
        actionListener.onClick(school)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SchoolItemBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return SchoolsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SchoolsViewHolder, position: Int) {
        val school = schools[position]
        with(holder.binding) {
            holder.itemView.tag = school
            schoolName.text = school.school
            schoolCity.text = school.city
        }
    }

    override fun getItemCount(): Int = schools.size
}