package com.mezhendosina.sgo.app.model.announcements

import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.data.requests.announcements.AnnouncementsResponseEntity
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