package com.mezhendosina.sgo.app

import android.app.Application
import com.mezhendosina.sgo.app.ui.lessonItem.LessonService
import com.mezhendosina.sgo.app.ui.login.chooseSchool.ChooseSchoolService
import com.mezhendosina.sgo.app.ui.main.AnnouncementsService
import com.mezhendosina.sgo.app.ui.main.GradeService
import com.mezhendosina.sgo.app.ui.main.TodayHomeworkService

class App : Application() {
    val todayHomeworkService = TodayHomeworkService()
    val announcementsService = AnnouncementsService()
    val chooseSchoolService = ChooseSchoolService()
    val gradesService = GradeService()
    val lessonService = LessonService()
}