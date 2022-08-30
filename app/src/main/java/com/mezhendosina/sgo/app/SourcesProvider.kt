package com.mezhendosina.sgo.app

import com.mezhendosina.sgo.app.model.announcements.AnnouncementsSource
import com.mezhendosina.sgo.app.model.journal.DiarySource
import com.mezhendosina.sgo.app.model.login.LoginSource
import com.mezhendosina.sgo.app.model.grades.GradesSource
import com.mezhendosina.sgo.app.model.homework.HomeworkSource
import com.mezhendosina.sgo.app.model.settings.SettingsSource

interface SourcesProvider {

    fun getLoginSource(): LoginSource

    fun getDiarySource(): DiarySource

    fun getHomeworkSource(): HomeworkSource

    fun getAnnouncementsSource(): AnnouncementsSource

    fun getSettingsSource(): SettingsSource

    fun getGradesSource(): GradesSource
}