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

package com.mezhendosina.sgo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mezhendosina.sgo.app.SourcesProvider
import com.mezhendosina.sgo.app.model.announcements.AnnouncementsRepository
import com.mezhendosina.sgo.app.model.announcements.AnnouncementsSource
import com.mezhendosina.sgo.app.model.attachments.AttachmentsRepository
import com.mezhendosina.sgo.app.model.chooseSchool.ChooseSchoolRepository
import com.mezhendosina.sgo.app.model.chooseSchool.SchoolUiEntity
import com.mezhendosina.sgo.app.model.chooseSchool.SchoolsSource
import com.mezhendosina.sgo.app.model.container.ContainerRepository
import com.mezhendosina.sgo.app.model.grades.GradesRepository
import com.mezhendosina.sgo.app.model.grades.GradesSource
import com.mezhendosina.sgo.app.model.homework.HomeworkSource
import com.mezhendosina.sgo.app.model.journal.DiarySource
import com.mezhendosina.sgo.app.model.journal.JournalRepository
import com.mezhendosina.sgo.app.model.journal.entities.DiaryUiEntity
import com.mezhendosina.sgo.app.model.journal.entities.LessonUiEntity
import com.mezhendosina.sgo.app.model.login.LoginRepository
import com.mezhendosina.sgo.app.model.login.LoginSource
import com.mezhendosina.sgo.app.model.settings.SettingsRepository
import com.mezhendosina.sgo.app.model.settings.SettingsSource
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.WeekStartEndEntity
import com.mezhendosina.sgo.data.requests.sgo.SourceProviderHolder
import com.mezhendosina.sgo.data.requests.sgo.announcements.AnnouncementsResponseEntity
import com.mezhendosina.sgo.data.requests.sgo.diary.entities.PastMandatoryEntity
import com.mezhendosina.sgo.data.requests.sgo.grades.entities.GradesItem
import com.mezhendosina.sgo.data.requests.sgo.grades.entities.gradeOptions.GradeOptions
import com.mezhendosina.sgo.data.requests.sgo.login.entities.StudentResponseEntity
import com.mezhendosina.sgo.data.requests.sgo.settings.entities.MySettingsResponseEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

object Singleton {

    private lateinit var applicationContext: Context

    var at: String = ""
    var announcements: List<AnnouncementsResponseEntity> = emptyList()

    var currentYearId = MutableLiveData<Int>()

    var users: List<StudentResponseEntity> = emptyList()
    var lesson: LessonUiEntity? = null
    var pastMandatoryItem: PastMandatoryEntity? = null


    var schools = mutableListOf<SchoolUiEntity>()
    val gradesOptions = MutableLiveData<GradeOptions>()
    var grades: List<GradesItem> = emptyList()
    val gradesRecyclerViewLoaded = MutableLiveData<Boolean>(true)

    var mySettings: MutableLiveData<MySettingsResponseEntity> = MutableLiveData()

    val weeks = mutableListOf<WeekStartEndEntity>()

    var currentWeek: Int? = null
    val loadedDiaryUiEntity: MutableList<DiaryUiEntity> = mutableListOf()
    val currentDiaryUiEntity = MutableLiveData<DiaryUiEntity>()
    val diaryRecyclerViewLoaded = MutableLiveData<Boolean>(true)

    var journalTabsLayout: TabLayout? = null
    var tabLayoutMediator: TabLayoutMediator? = null
    var baseUrl = ""


    private val sourcesProvider: SourcesProvider by lazy {
        SourceProviderHolder.sourcesProvider
    }

    // --- sources
    private val schoolsSource: SchoolsSource by lazy {
        sourcesProvider.getSchoolsSource()
    }

    private val loginSource: LoginSource by lazy {
        sourcesProvider.getLoginSource()
    }

    private val diarySource: DiarySource by lazy {
        sourcesProvider.getDiarySource()
    }

    val homeworkSource: HomeworkSource by lazy {
        sourcesProvider.getHomeworkSource()
    }

    private val announcementsSource: AnnouncementsSource by lazy {
        sourcesProvider.getAnnouncementsSource()
    }

    private val gradesSource: GradesSource by lazy {
        sourcesProvider.getGradesSource()
    }

    private val settingsSource: SettingsSource by lazy {
        sourcesProvider.getSettingsSource()
    }

    // --- repositories

    val chooseSchoolRepository: ChooseSchoolRepository by lazy {
        ChooseSchoolRepository(schoolsSource)
    }

    val loginRepository: LoginRepository by lazy {
        LoginRepository(loginSource, settingsSource)
    }

    val announcementsRepository by lazy {
        AnnouncementsRepository(announcementsSource)
    }

    val gradesRepository by lazy {
        GradesRepository(gradesSource)
    }

    val settingsRepository by lazy {
        SettingsRepository(settingsSource)
    }

    val containerRepository by lazy {
        ContainerRepository()
    }

    val attachmentsRepository by lazy {
        AttachmentsRepository(homeworkSource)
    }

    val journalRepository by lazy {
        JournalRepository(homeworkSource, diarySource)
    }

    // --- database
//    val database: AppDatabase by lazy {
//        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database.db").build()
//    }

    const val ANNOUNCEMENTS_ID = "announcementsID"


    fun loadContext(context: Context) {
        applicationContext = context
        CoroutineScope(Dispatchers.IO).launch {
            val settings = Settings(context)
            baseUrl = settings.regionUrl.first() ?: ""
        }
    }

    fun getContext(): Context = applicationContext

}

