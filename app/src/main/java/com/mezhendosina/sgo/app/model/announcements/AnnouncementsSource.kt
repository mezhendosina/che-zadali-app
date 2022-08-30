package com.mezhendosina.sgo.app.model.announcements

import com.mezhendosina.sgo.data.requests.announcements.AnnouncementsResponseEntity

interface AnnouncementsSource {
    suspend fun getAnnouncements(): List<AnnouncementsResponseEntity>
}