package com.mezhendosina.sgo.app.ui.chooseUserId

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.ItemUserIdBinding
import com.mezhendosina.sgo.app.model.user.UserUIEntity

class UserIdAdapter(
    private val onUserClickListener: (Int) -> Unit
) : RecyclerView.Adapter<UserIdAdapter.UserIdViewHolder>(), View.OnClickListener {
    var users: List<UserUIEntity> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class UserIdViewHolder(val binding: ItemUserIdBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onClick(v: View) {
        val user = v.tag as UserUIEntity

        onUserClickListener.invoke(user.userId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserIdViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserIdBinding.inflate(inflater)

        binding.root.setOnClickListener(this)

        return UserIdViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserIdViewHolder, position: Int) {
        val user = users[position]

        holder.itemView.tag = user

        with(holder.binding) {
            userName.text = user.name
            login.text = user.login
            //TODO profile photo
        }
    }

    override fun getItemCount(): Int = users.size
}