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

package com.mezhendosina.sgo.app.ui.announcementsBottomSheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.ItemAnnouncementBinding
import com.mezhendosina.sgo.data.requests.sgo.announcements.AnnouncementsResponseEntity
import org.jsoup.Jsoup

typealias OnAnnouncementClickListener = (AnnouncementsResponseEntity) -> Unit

class AnnouncementsAdapter(
    private val onAnnouncementClickListener: OnAnnouncementClickListener,
) :
    RecyclerView.Adapter<AnnouncementsAdapter.AnnouncementsViewHolder>(),
    View.OnClickListener {

    var announcements: List<AnnouncementsResponseEntity> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }


    class AnnouncementsViewHolder(val binding: ItemAnnouncementBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onClick(p0: View) {
        val announcement = p0.tag as AnnouncementsResponseEntity
        onAnnouncementClickListener(announcement)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAnnouncementBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return AnnouncementsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnnouncementsViewHolder, position: Int) {
        val announcement = announcements[position]
        with(holder.binding) {
            holder.itemView.tag = announcement
            announcementHeader.text = announcement.name.parseAsHtml()
            val a = Jsoup.parse(announcement.description).text()
            announcementBody.text = Jsoup.parse(a).text()
        }
    }

    override fun getItemCount(): Int = announcements.size
}
