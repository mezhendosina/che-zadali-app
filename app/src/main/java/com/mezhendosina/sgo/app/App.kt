package com.mezhendosina.sgo.app

import android.app.Application
import com.mezhendosina.sgo.app.ui.journal.JournalService
import com.mezhendosina.sgo.app.ui.main.AnnouncementsService
import com.mezhendosina.sgo.app.ui.main.TodayHomeworkService

class App : Application() {
    val todayHomeworkService = TodayHomeworkService()
    val announcementsService = AnnouncementsService()
    val journalService = JournalService()

}