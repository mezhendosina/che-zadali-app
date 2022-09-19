package com.mezhendosina.sgo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.mezhendosina.sgo.app.SourcesProvider
import com.mezhendosina.sgo.app.model.announcements.AnnouncementsRepository
import com.mezhendosina.sgo.app.model.announcements.AnnouncementsSource
import com.mezhendosina.sgo.app.model.attachments.AttachmentsRepository
import com.mezhendosina.sgo.app.model.chooseSchool.ChooseSchoolRepository
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
import com.mezhendosina.sgo.data.WeekStartEndEntity
import com.mezhendosina.sgo.data.currentWeekStart
import com.mezhendosina.sgo.data.requests.SourceProviderHolder
import com.mezhendosina.sgo.data.requests.announcements.AnnouncementsResponseEntity
import com.mezhendosina.sgo.data.requests.grades.entities.GradesItem
import com.mezhendosina.sgo.data.requests.grades.entities.gradeOptions.GradeOptions
import com.mezhendosina.sgo.data.requests.other.entities.schools.SchoolItem
import com.mezhendosina.sgo.data.requests.settings.entities.MySettingsResponseEntity
import com.mezhendosina.sgo.data.room.AppDatabase

object Singleton {

    private lateinit var applicationContext: Context

    var at: String = ""
    var announcements: List<AnnouncementsResponseEntity> = emptyList()

    val updateDiary = MutableLiveData<Boolean>(false)

    var currentYearId = MutableLiveData<Int>()
    var diaryEntity: DiaryUiEntity = DiaryUiEntity(
        emptyList(),
        "",
        "",
        emptyList()
    )

    var lesson = LessonUiEntity(
        emptyList(),
        null,
        0,
        "",
        "",
        false,
        0,
        0,
        "",
        ""
    )
    var schools = mutableListOf<SchoolItem>()

    var gradesOptions: GradeOptions? = null
    var grades: List<GradesItem> = emptyList()

    var mySettings: MySettingsResponseEntity? = null

    val transition = MutableLiveData<Boolean>(null)
    private val sourcesProvider: SourcesProvider by lazy {
        SourceProviderHolder.sourcesProvider
    }

    val weeks = mutableListOf<WeekStartEndEntity>()
    val currentWeek = currentWeekStart()

    // --- sources
    private val loginSource: LoginSource by lazy {
        sourcesProvider.getLoginSource()
    }

    val diarySource: DiarySource by lazy {
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
        ChooseSchoolRepository()
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
    val database: AppDatabase by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database.db")
            .build()
    }
    val ANNOUNCEMENTS_ID = "announcementsID"


    fun loadContext(context: Context) {
        applicationContext = context
    }

    fun getContext(): Context = applicationContext


}

