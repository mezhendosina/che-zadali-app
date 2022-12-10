package com.mezhendosina.sgo.data.requests.sgo.announcements

import com.mezhendosina.sgo.app.model.announcements.AnnouncementsSource
import com.mezhendosina.sgo.data.requests.sgo.base.BaseRetrofitSource
import com.mezhendosina.sgo.data.requests.sgo.base.RetrofitConfig

class RetrofitAnnouncementsSource(
    config: RetrofitConfig
) : BaseRetrofitSource(config), AnnouncementsSource {

    private val announcementsApi = retrofit.create(AnnouncementsApi::class.java)

    override suspend fun getAnnouncements(): List<AnnouncementsResponseEntity> =
        wrapRetrofitExceptions {
            announcementsApi.getAnnouncements()
        }
}