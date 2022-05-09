package com.mezhendosina.sgo.app.ui.main

import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.data.announcements.AnnouncementsResponseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

typealias AnnouncementsActionListener = (announcements: List<AnnouncementsResponseItem>) -> Unit

class AnnouncementsService {

    private var announcements = mutableListOf<AnnouncementsResponseItem>()

    private val listeners = mutableSetOf<AnnouncementsActionListener>()

    suspend fun announcements() {
        val requests = Singleton.requests
        announcements = requests.announcements(Singleton.at).toMutableList()
        announcements.forEach { println(Jsoup.parse(it.description).text()) }
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