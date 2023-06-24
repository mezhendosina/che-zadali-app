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

package com.mezhendosina.sgo.app.ui.loginFlow.chooseUserId

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.ItemUserIdBinding
import com.mezhendosina.sgo.app.uiEntities.UserUIEntity

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