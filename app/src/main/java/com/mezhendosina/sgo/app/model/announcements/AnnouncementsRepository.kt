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

package com.mezhendosina.sgo.app.model.announcements

import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.data.netschool.api.announcements.AnnouncementsResponseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

typealias AnnouncementsActionListener = (announcements: List<AnnouncementsResponseEntity>) -> Unit

class AnnouncementsRepository(
    private val announcementsSource: AnnouncementsSource
) {
    private var announcements = mutableListOf<AnnouncementsResponseEntity>()

    private val listeners = mutableSetOf<AnnouncementsActionListener>()

    suspend fun announcements() {
        announcements = announcementsSource.getAnnouncements().toMutableList()
        Singleton.announcements = announcements
        withContext(Dispatchers.Main) {
            notifyListeners()
        }
    }

    fun addListener(listener: AnnouncementsActionListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: AnnouncementsActionListener) {
        listeners.remove(listener)
    }

    private fun notifyListeners() {
        listeners.forEach { it.invoke(announcements) }
    }
}