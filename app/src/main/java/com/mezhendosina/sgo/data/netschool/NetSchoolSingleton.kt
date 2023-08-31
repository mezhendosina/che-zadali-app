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

package com.mezhendosina.sgo.data.netschool

import androidx.lifecycle.MutableLiveData
import com.mezhendosina.sgo.app.model.ContainerRepository
import com.mezhendosina.sgo.app.model.announcements.AnnouncementsRepository
import com.mezhendosina.sgo.app.model.announcements.AnnouncementsSource
import com.mezhendosina.sgo.app.model.answer.AnswerRepository
import com.mezhendosina.sgo.app.model.attachments.AttachmentsRepository
import com.mezhendosina.sgo.app.model.grades.GradesRepository
import com.mezhendosina.sgo.app.model.grades.GradesSource
import com.mezhendosina.sgo.app.model.journal.DiarySource
import com.mezhendosina.sgo.app.model.journal.JournalRepository
import com.mezhendosina.sgo.app.uiEntities.AssignTypeUiEntity
import com.mezhendosina.sgo.app.uiEntities.SchoolUiEntity
import com.mezhendosina.sgo.data.netschool.api.attachments.AttachmentsSource
import com.mezhendosina.sgo.data.netschool.api.homework.HomeworkSource
import com.mezhendosina.sgo.data.netschool.api.login.LoginSource
import com.mezhendosina.sgo.data.netschool.api.settings.SettingsSource
import com.mezhendosina.sgo.data.netschool.base.SourceProviderHolder
import com.mezhendosina.sgo.data.netschool.base.SourcesProvider
import com.mezhendosina.sgo.data.netschool.repo.LessonRepository
import com.mezhendosina.sgo.data.netschool.repo.LoginRepository
import com.mezhendosina.sgo.data.netschool.repo.RegionsRepository
import com.mezhendosina.sgo.data.netschool.repo.SettingsRepository


object NetSchoolSingleton {
    var baseUrl = ""
    var at = ""
    val journalYearId = MutableLiveData<Int>()
    var assignTypes: List<AssignTypeUiEntity>? = null
    var schools = emptyList<SchoolUiEntity>()
    private val sourcesProvider: SourcesProvider by lazy {
        SourceProviderHolder.sourcesProvider
    }


    // --- sources

    private val loginSource: LoginSource by lazy {
        sourcesProvider.getLoginSource()
    }

    private val settingsSource: SettingsSource by lazy {
        sourcesProvider.getSettingsSource()
    }

    private val attachmentsSource: AttachmentsSource by lazy {
        sourcesProvider.getAttachmentsSource()
    }

    private val diarySource: DiarySource by lazy {
        sourcesProvider.getDiarySource()
    }
    private val homeworkSource: HomeworkSource by lazy {
        sourcesProvider.getHomeworkSource()
    }
    private val announcementsSource: AnnouncementsSource by lazy {
        sourcesProvider.getAnnouncementsSource()
    }
    private val gradesSource: GradesSource by lazy {
        sourcesProvider.getGradesSource()
    }
    // --- repositories

    val regionsRepository: RegionsRepository by lazy {
        RegionsRepository()
    }

    val loginRepository: LoginRepository by lazy {
        LoginRepository(loginSource, settingsSource)
    }

    val announcementsRepository by lazy {
        AnnouncementsRepository(announcementsSource)
    }

    val gradesRepository by lazy {
        GradesRepository(gradesSource, diarySource)
    }

    val settingsRepository by lazy {
        SettingsRepository(settingsSource)
    }

    val githubUpdateDownloader by lazy {
        sourcesProvider.getGithubUpdateDownloader()
    }

    val containerRepository by lazy {
        ContainerRepository()
    }

    val attachmentsRepository by lazy {
        AttachmentsRepository(attachmentsSource)
    }

    val journalRepository by lazy {
        JournalRepository(attachmentsSource, diarySource)
    }

    val answerRepository by lazy {
        AnswerRepository(attachmentsSource, lessonRepository)
    }

    val lessonRepository by lazy {
        LessonRepository(homeworkSource, attachmentsSource)
    }

}