package com.mezhendosina.sgo.data.requests.announcements

import retrofit2.http.GET

interface AnnouncementsApi {
    @GET("webapi/announcements")
    suspend fun getAnnouncements(): List<AnnouncementsResponseEntity>
}