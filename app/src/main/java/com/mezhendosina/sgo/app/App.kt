package com.mezhendosina.sgo.app

import android.app.Application
import com.mezhendosina.sgo.app.ui.journal.lessonItem.LessonService
import com.mezhendosina.sgo.app.ui.chooseSchool.ChooseSchoolService
import com.mezhendosina.sgo.app.ui.bottomSheets.annoucements.AnnouncementsService
import com.mezhendosina.sgo.app.ui.grades.GradeService

class App : Application() {
    val announcementsService = AnnouncementsService()
    val chooseSchoolService = ChooseSchoolService()
    val gradesService = GradeService()
    val lessonService = LessonService()
}